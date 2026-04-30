package dev.yhiguchi.home_expense.infrastructure.datasource.income;

import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.infrastructure.datasource.ReadOnly;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.ParameterBinder;
import dev.yhiguchi.home_expense.query.income.IncomeDetail;
import dev.yhiguchi.home_expense.query.income.IncomeSearchCriteria;
import dev.yhiguchi.home_expense.query.income.IncomeSearchResult;
import dev.yhiguchi.home_expense.query.income.IncomeSearchResultQuerier;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class IncomeSearchResultDataSource implements IncomeSearchResultQuerier {

  private static final String SELECT_COLUMNS =
      """
      SELECT
        income.id,
        income.description,
        income.amount,
        income.receive_date,
        income.version,
        income_attribute.id   AS attribute_id,
        income_attribute.name AS attribute_name
      FROM expense.income
      INNER JOIN expense.income_attribute
        ON income_attribute.id = income.attribute_id
      """;

  private final JdbcOperator jdbc;

  public IncomeSearchResultDataSource(@ReadOnly JdbcOperator jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public IncomeSearchResult search(IncomeSearchCriteria criteria) {
    int totalCount = selectCount(criteria);
    if (totalCount == 0) {
      return IncomeSearchResult.empty();
    }
    List<IncomeDetail> list = selectBy(criteria);
    return new IncomeSearchResult(totalCount, list);
  }

  @Override
  public Optional<IncomeDetail> find(IncomeIdentifier id) {
    String sql = SELECT_COLUMNS + " WHERE income.id = ?";
    return jdbc.queryForOptional(
        sql, ParameterBinder.of(id.value()), IncomeSearchResultDataSource::mapDetail);
  }

  private int selectCount(IncomeSearchCriteria criteria) {
    StringBuilder sql =
        new StringBuilder(
            """
            SELECT COUNT(*)
            FROM expense.income
            INNER JOIN expense.income_attribute
              ON income_attribute.id = income.attribute_id
            """);
    List<Object> params = new ArrayList<>();
    appendDateRange(criteria, sql, params);
    return jdbc.queryForOptional(
            sql.toString(), ParameterBinder.of(params.toArray()), rs -> rs.getInt(1))
        .orElse(0);
  }

  private List<IncomeDetail> selectBy(IncomeSearchCriteria criteria) {
    StringBuilder sql = new StringBuilder(SELECT_COLUMNS);
    List<Object> params = new ArrayList<>();
    appendDateRange(criteria, sql, params);
    sql.append(" ORDER BY income.receive_date DESC");
    sql.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
    params.add(criteria.offset());
    params.add(criteria.perPage());
    return jdbc.queryForList(
        sql.toString(),
        ParameterBinder.of(params.toArray()),
        IncomeSearchResultDataSource::mapDetail);
  }

  private void appendDateRange(
      IncomeSearchCriteria criteria, StringBuilder sql, List<Object> params) {
    if (criteria.hasDateRange()) {
      sql.append(" WHERE ");
      sql.append("income.receive_date >= ? AND income.receive_date < ?");
      params.add(criteria.dateFrom());
      params.add(criteria.dateTo());
    }
  }

  static IncomeDetail mapDetail(ResultSet rs) throws SQLException {
    return new IncomeDetail(
        rs.getString("id"),
        rs.getString("description"),
        rs.getInt("amount"),
        rs.getObject("receive_date", LocalDate.class),
        rs.getString("attribute_id"),
        rs.getString("attribute_name"),
        rs.getLong("version"));
  }
}
