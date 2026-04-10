package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

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
public class ExpenseAggregateDataSource implements ExpenseAggregateRepository {

  DataSource dataSource;

  public ExpenseAggregateDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public ExpenseAggregate find(ExpenseAggregateCriteria criteria) {
    try (Connection conn = dataSource.getConnection()) {
      int incomeTotalAmount = selectIncomeTotalAmount(conn, criteria).orElse(0);
      List<ExpenseAttributeAggregate> fixedAggregates =
          selectByCategory(conn, "fixed_expense", criteria);
      List<ExpenseAttributeAggregate> variableAggregates =
          selectByCategory(conn, "variable_expense", criteria);
      return new ExpenseAggregate(
          incomeTotalAmount,
          new ExpenseAggregateDetail(fixedAggregates),
          new ExpenseAggregateDetail(variableAggregates));
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  private Optional<Integer> selectIncomeTotalAmount(
      Connection conn, ExpenseAggregateCriteria criteria) throws SQLException {
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
          int value = rs.getInt(1);
          if (rs.wasNull()) {
            return Optional.empty();
          }
          return Optional.of(value);
        }
        return Optional.empty();
      }
    }
  }

  private List<ExpenseAttributeAggregate> selectByCategory(
      Connection conn, String junctionTable, ExpenseAggregateCriteria criteria)
      throws SQLException {
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
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setDate(1, Date.valueOf(criteria.dateFrom()));
      ps.setDate(2, Date.valueOf(criteria.dateTo()));
      try (ResultSet rs = ps.executeQuery()) {
        List<ExpenseAttributeAggregate> list = new ArrayList<>();
        while (rs.next()) {
          list.add(
              new ExpenseAttributeAggregate(
                  new ExpenseAttributeIdentifier(rs.getString("id")),
                  new ExpenseAttributeName(rs.getString("name")),
                  rs.getInt("total_amount")));
        }
        return list;
      }
    }
  }
}
