package dev.yhiguchi.home_expense.infrastructure.datasource.income;

import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.ParameterBinder;
import dev.yhiguchi.home_expense.query.income.IncomeSearchCriteria;
import dev.yhiguchi.home_expense.query.income.IncomeSearchResult;
import dev.yhiguchi.home_expense.query.income.IncomeSearchResultQuerier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

@ApplicationScoped
@Transactional
public class IncomeSearchResultDataSource implements IncomeSearchResultQuerier {

  private final JdbcOperator jdbc;

  public IncomeSearchResultDataSource(
      @io.quarkus.agroal.DataSource("readonly") DataSource dataSource) {
    this.jdbc = new JdbcOperator(dataSource);
  }

  @Override
  public IncomeSearchResult find(IncomeSearchCriteria criteria) {
    int totalCount = selectCount(criteria);
    if (totalCount == 0) {
      return new IncomeSearchResult();
    }
    List<Income> list = selectBy(criteria);
    return new IncomeSearchResult(totalCount, list);
  }

  private int selectCount(IncomeSearchCriteria criteria) {
    StringBuilder sql =
        new StringBuilder(
            """
            SELECT COUNT(*)
            FROM expense.income
            LEFT JOIN income_attribute
              ON income_attribute.id = income.attribute_id
            """);
    List<Object> params = new ArrayList<>();
    appendDateRange(criteria, sql, params);
    return jdbc.queryForOptional(sql.toString(), ParameterBinder.of(params), rs -> rs.getInt(1))
        .orElse(0);
  }

  private List<Income> selectBy(IncomeSearchCriteria criteria) {
    StringBuilder sql =
        new StringBuilder(
            """
            SELECT
              income.id,
              income.description,
              income.amount,
              income.receive_date,
              income.version,
              income_attribute.id AS attribute_id,
              income_attribute.name AS attribute_name
            FROM expense.income
            LEFT JOIN income_attribute
              ON income_attribute.id = income.attribute_id
            """);
    List<Object> params = new ArrayList<>();
    appendDateRange(criteria, sql, params);
    sql.append(" ORDER BY income.receive_date DESC");
    sql.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
    params.add(criteria.offset());
    params.add(criteria.perPage());
    return jdbc.queryForList(
        sql.toString(), ParameterBinder.of(params), IncomeDataSource::mapIncome);
  }

  private void appendDateRange(
      IncomeSearchCriteria criteria, StringBuilder sql, List<Object> params) {
    if (criteria.hasDateRange()) {
      sql.append(" WHERE ");
      sql.append("income.receive_date >= ? AND income.receive_date < ?");
      params.add(Date.valueOf(criteria.dateFrom()));
      params.add(Date.valueOf(criteria.dateTo()));
    }
  }
}
