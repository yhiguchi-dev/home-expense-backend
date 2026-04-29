package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.infrastructure.datasource.ReadOnly;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.ParameterBinder;
import dev.yhiguchi.home_expense.query.expense.ExpenseDetail;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResult;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResultQuerier;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ExpenseSearchResultDataSource implements ExpenseSearchResultQuerier {

  private static final String SELECT_COLUMNS =
      """
      SELECT
        expense.id,
        expense.description,
        expense.amount,
        expense.payment_date,
        expense.version,
        attribute.id   AS attribute_id,
        attribute.name AS attribute_name,
        attribute.category
      FROM expense.expense
      INNER JOIN expense.attribute
        ON expense.attribute_id = attribute.id
      """;

  private final JdbcOperator jdbc;

  public ExpenseSearchResultDataSource(@ReadOnly JdbcOperator jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public ExpenseSearchResult search(ExpenseSearchCriteria criteria) {
    int totalCount = selectCount(criteria);
    if (totalCount == 0) {
      return ExpenseSearchResult.empty();
    }
    List<ExpenseDetail> list = selectBy(criteria);
    return new ExpenseSearchResult(totalCount, list);
  }

  @Override
  public Optional<ExpenseDetail> find(ExpenseIdentifier id) {
    String sql = SELECT_COLUMNS + " WHERE expense.id = ?";
    return jdbc.queryForOptional(
        sql, ParameterBinder.of(id.value()), ExpenseSearchResultDataSource::mapDetail);
  }

  private int selectCount(ExpenseSearchCriteria criteria) {
    StringBuilder sql =
        new StringBuilder(
            """
            SELECT COUNT(*)
            FROM expense.expense
            INNER JOIN expense.attribute
              ON expense.attribute_id = attribute.id
            """);
    List<Object> params = new ArrayList<>();
    appendWhere(criteria, sql, params);
    return jdbc.queryForOptional(
            sql.toString(), ParameterBinder.of(params.toArray()), rs -> rs.getInt(1))
        .orElse(0);
  }

  private List<ExpenseDetail> selectBy(ExpenseSearchCriteria criteria) {
    StringBuilder sql = new StringBuilder(SELECT_COLUMNS);
    List<Object> params = new ArrayList<>();
    appendWhere(criteria, sql, params);
    sql.append(" ORDER BY attribute.category DESC, expense.payment_date DESC");
    sql.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
    params.add(criteria.pagination().offset());
    params.add(criteria.pagination().perPage());
    return jdbc.queryForList(
        sql.toString(),
        ParameterBinder.of(params.toArray()),
        ExpenseSearchResultDataSource::mapDetail);
  }

  private void appendWhere(ExpenseSearchCriteria criteria, StringBuilder sql, List<Object> params) {
    List<String> conditions = new ArrayList<>();
    if (criteria.hasDateRange()) {
      conditions.add("expense.payment_date >= ?");
      conditions.add("expense.payment_date < ?");
      params.add(criteria.dateFrom());
      params.add(criteria.dateTo());
    }
    if (criteria.hasExpenseCategory()) {
      conditions.add("attribute.category = ?");
      params.add(criteria.expenseCategory());
    }
    if (criteria.hasExpenseAttributeIdentifier()) {
      conditions.add("attribute.id = ?");
      params.add(criteria.expenseAttributeIdentifier());
    }
    if (!conditions.isEmpty()) {
      sql.append(" WHERE ");
      sql.append(String.join(" AND ", conditions));
    }
  }

  static ExpenseDetail mapDetail(ResultSet rs) throws SQLException {
    return new ExpenseDetail(
        rs.getString("id"),
        rs.getString("description"),
        rs.getInt("amount"),
        rs.getObject("payment_date", LocalDate.class),
        rs.getString("attribute_id"),
        rs.getString("attribute_name"),
        rs.getString("category"),
        rs.getLong("version"));
  }
}
