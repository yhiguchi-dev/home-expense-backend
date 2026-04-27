package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.Amount;
import dev.yhiguchi.home_expense.domain.model.expense.Description;
import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.PaymentDate;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.ParameterBinder;
import dev.yhiguchi.home_expense.query.expense.ExpenseDetail;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResult;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResultQuerier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

@ApplicationScoped
@Transactional
public class ExpenseSearchResultDataSource implements ExpenseSearchResultQuerier {

  private final JdbcOperator jdbc;

  public ExpenseSearchResultDataSource(
      @io.quarkus.agroal.DataSource("readonly") DataSource dataSource) {
    this.jdbc = new JdbcOperator(dataSource);
  }

  @Override
  public ExpenseSearchResult find(ExpenseSearchCriteria criteria) {
    int totalCount = selectCount(criteria);
    if (totalCount == 0) {
      return new ExpenseSearchResult();
    }
    List<ExpenseDetail> list = selectBy(criteria);
    return new ExpenseSearchResult(totalCount, list);
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
    return jdbc.queryForOptional(sql.toString(), ParameterBinder.of(params), rs -> rs.getInt(1))
        .orElse(0);
  }

  private List<ExpenseDetail> selectBy(ExpenseSearchCriteria criteria) {
    StringBuilder sql =
        new StringBuilder(
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
            """);
    List<Object> params = new ArrayList<>();
    appendWhere(criteria, sql, params);
    sql.append(" ORDER BY attribute.category DESC, expense.payment_date DESC");
    sql.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
    params.add(criteria.pagination().offset());
    params.add(criteria.pagination().perPage());
    return jdbc.queryForList(
        sql.toString(), ParameterBinder.of(params), ExpenseSearchResultDataSource::mapDetail);
  }

  private void appendWhere(ExpenseSearchCriteria criteria, StringBuilder sql, List<Object> params) {
    List<String> conditions = new ArrayList<>();
    if (criteria.hasDateRange()) {
      conditions.add("expense.payment_date >= ?");
      conditions.add("expense.payment_date < ?");
      params.add(Date.valueOf(criteria.dateFrom()));
      params.add(Date.valueOf(criteria.dateTo()));
    }
    if (criteria.hasExpenseCategory()) {
      conditions.add("attribute.category = ?");
      params.add(criteria.getExpenseCategory().name());
    }
    if (criteria.hasExpenseAttributeIdentifier()) {
      conditions.add("attribute.id = ?");
      params.add(criteria.getExpenseAttributeIdentifier());
    }
    if (!conditions.isEmpty()) {
      sql.append(" WHERE ");
      sql.append(String.join(" AND ", conditions));
    }
  }

  static ExpenseDetail mapDetail(ResultSet rs) throws SQLException {
    Expense expense =
        new Expense(
            new ExpenseIdentifier(rs.getString("id")),
            new Description(rs.getString("description")),
            new Amount(rs.getInt("amount")),
            new PaymentDate(rs.getObject("payment_date", LocalDate.class)),
            new ExpenseAttributeIdentifier(rs.getString("attribute_id")));
    ExpenseAttribute attribute =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier(rs.getString("attribute_id")),
            new ExpenseAttributeName(rs.getString("attribute_name")),
            ExpenseCategory.valueOf(rs.getString("category")));
    return new ExpenseDetail(expense, attribute, rs.getLong("version"));
  }
}
