package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import dev.yhiguchi.home_expense.query.expense.*;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

@ApplicationScoped
public class ExpenseStatisticsDataSource implements ExpenseStatisticsQuerier {

  DataSource dataSource;

  public ExpenseStatisticsDataSource(
      @io.quarkus.agroal.DataSource("readonly") DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public ExpenseStatistics find(ExpenseStatisticsCriteria criteria) {
    try (Connection conn = dataSource.getConnection()) {
      long incomeTotalAmount = selectIncomeTotalAmount(conn, criteria).orElse(0L);
      List<ExpenseAttributeStatistics> fixedStatistics =
          selectByCategory(conn, ExpenseCategory.固定費, criteria);
      List<ExpenseAttributeStatistics> variableStatistics =
          selectByCategory(conn, ExpenseCategory.変動費, criteria);
      return new ExpenseStatistics(
          incomeTotalAmount,
          new ExpenseStatisticsDetail(fixedStatistics),
          new ExpenseStatisticsDetail(variableStatistics));
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  private Optional<Long> selectIncomeTotalAmount(
      Connection conn, ExpenseStatisticsCriteria criteria) throws SQLException {
    String sql =
        """
        SELECT sum(income.amount)
        FROM expense.income
        WHERE income.receive_date >= ? AND income.receive_date < ?
        """;
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setDate(1, Date.valueOf(criteria.dateFrom()));
      ps.setDate(2, Date.valueOf(criteria.dateTo()));
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          long value = rs.getLong(1);
          if (rs.wasNull()) {
            return Optional.empty();
          }
          return Optional.of(value);
        }
        return Optional.empty();
      }
    }
  }

  private List<ExpenseAttributeStatistics> selectByCategory(
      Connection conn, ExpenseCategory category, ExpenseStatisticsCriteria criteria)
      throws SQLException {
    String sql = getString(category);
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setDate(1, Date.valueOf(criteria.dateFrom()));
      ps.setDate(2, Date.valueOf(criteria.dateTo()));
      try (ResultSet rs = ps.executeQuery()) {
        List<ExpenseAttributeStatistics> list = new ArrayList<>();
        while (rs.next()) {
          list.add(
              new ExpenseAttributeStatistics(
                  new ExpenseAttributeIdentifier(rs.getString("id")),
                  new ExpenseAttributeName(rs.getString("name")),
                  rs.getLong("total_amount")));
        }
        return list;
      }
    }
  }

  private static String getString(ExpenseCategory category) {
    String junctionTable =
        switch (category) {
          case 固定費 -> "fixed_expense";
          case 変動費 -> "variable_expense";
        };
    String sql =
        """
        SELECT
          attribute.id,
          attribute.name,
          sum(expense.expense.price) AS total_amount
        FROM expense.expense
        LEFT JOIN %s
          ON expense.id = %s.expense_id
        LEFT JOIN attribute
          ON attribute.id = %s.attribute_id
        WHERE attribute.id IS NOT NULL
        AND expense.payment_date >= ? AND expense.payment_date < ?
        GROUP BY attribute.id, attribute.name, attribute.created_at
        ORDER BY attribute.created_at
        """
            .formatted(junctionTable, junctionTable, junctionTable);
    return sql;
  }
}
