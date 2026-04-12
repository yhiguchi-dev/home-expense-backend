package dev.yhiguchi.home_expense.infrastructure.datasource.income;

import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import dev.yhiguchi.home_expense.query.income.IncomeSearchCriteria;
import dev.yhiguchi.home_expense.query.income.IncomeSearchResult;
import dev.yhiguchi.home_expense.query.income.IncomeSearchResultQuerier;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

@ApplicationScoped
public class IncomeSearchResultDataSource implements IncomeSearchResultQuerier {

  DataSource dataSource;

  public IncomeSearchResultDataSource(
      @io.quarkus.agroal.DataSource("readonly") DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public IncomeSearchResult find(IncomeSearchCriteria criteria) {
    StringBuilder sql = new StringBuilder();
    sql.append(
        """
        SELECT
          income.id,
          income.description,
          income.amount,
          income.receive_date,
          income.version,
          income_attribute.id AS attribute_id,
          income_attribute.name AS attribute_name,
          COUNT(*) OVER() AS total_count
        FROM expense.income
        LEFT JOIN income_attribute
          ON income_attribute.id = income.attribute_id
        WHERE 1=1
        """);
    List<Object> params = new ArrayList<>();
    appendDateRange(criteria, sql, params);
    sql.append(" ORDER BY income.receive_date DESC");
    sql.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
    params.add(criteria.offset());
    params.add(criteria.perPage());
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql.toString())) {
      setParams(ps, params);
      try (ResultSet rs = ps.executeQuery()) {
        List<Income> list = new ArrayList<>();
        int totalCount = 0;
        while (rs.next()) {
          list.add(IncomeDataSource.mapIncome(rs));
          totalCount = rs.getInt("total_count");
        }
        if (list.isEmpty()) {
          return new IncomeSearchResult();
        }
        return new IncomeSearchResult(totalCount, list);
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  private void appendDateRange(
      IncomeSearchCriteria criteria, StringBuilder sql, List<Object> params) {
    if (criteria.hasDateRange()) {
      sql.append(" AND income.receive_date >= ? AND income.receive_date < ?");
      params.add(Date.valueOf(criteria.dateFrom()));
      params.add(Date.valueOf(criteria.dateTo()));
    }
  }

  private void setParams(PreparedStatement ps, List<Object> params) throws SQLException {
    for (int i = 0; i < params.size(); i++) {
      Object param = params.get(i);
      if (param instanceof Integer intVal) {
        ps.setInt(i + 1, intVal);
      } else if (param instanceof String strVal) {
        ps.setString(i + 1, strVal);
      } else if (param instanceof Date dateVal) {
        ps.setDate(i + 1, dateVal);
      }
    }
  }
}
