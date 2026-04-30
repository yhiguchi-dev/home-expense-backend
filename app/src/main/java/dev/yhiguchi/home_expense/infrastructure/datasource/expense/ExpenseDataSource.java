package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.Amount;
import dev.yhiguchi.home_expense.domain.model.Description;
import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.ParameterBinder;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
public class ExpenseDataSource implements ExpenseRepository {

  private static final long INITIAL_VERSION = 1L;

  private final JdbcOperator jdbc;

  public ExpenseDataSource(JdbcOperator jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public void register(Expense expense) {
    jdbc.update(
        "INSERT INTO expense.expense(id, description, amount, payment_date, attribute_id, version)"
            + " VALUES (?, ?, ?, ?, ?, ?)",
        ParameterBinder.of(
            expense.expenseIdentifier().value(),
            expense.description().value(),
            expense.amount().value(),
            expense.paymentDate().value(),
            expense.expenseAttributeIdentifier().value(),
            INITIAL_VERSION));
  }

  @Override
  public Optional<Expense> find(ExpenseIdentifier expenseIdentifier) {
    String sql =
        """
        SELECT id, description, amount, payment_date, attribute_id
        FROM expense.expense
        WHERE id = ?
        """;
    return jdbc.queryForOptional(
        sql, ParameterBinder.of(expenseIdentifier.value()), ExpenseDataSource::mapExpense);
  }

  @Override
  public boolean existsByAttributeIdentifier(
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    String sql =
        """
        SELECT CASE WHEN EXISTS(
          SELECT 1 FROM expense.expense WHERE attribute_id = ?
        ) THEN TRUE ELSE FALSE END
        FROM (VALUES (1)) AS t(x)
        """;
    return jdbc.queryForOptional(
            sql, ParameterBinder.of(expenseAttributeIdentifier.value()), rs -> rs.getBoolean(1))
        .orElse(false);
  }

  @Override
  public void update(Expense expense, long expectedVersion) {
    jdbc.updateWithOptimisticLock(
        """
        UPDATE expense.expense
        SET description = ?, amount = ?, payment_date = ?, attribute_id = ?, version = version + 1
        WHERE id = ? AND version = ?
        """,
        ParameterBinder.of(
            expense.description().value(),
            expense.amount().value(),
            expense.paymentDate().value(),
            expense.expenseAttributeIdentifier().value(),
            expense.expenseIdentifier().value(),
            expectedVersion));
  }

  @Override
  public void delete(Expense expense) {
    jdbc.update(
        "DELETE FROM expense.expense WHERE expense.id = ?",
        ParameterBinder.of(expense.expenseIdentifier().value()));
  }

  static Expense mapExpense(ResultSet rs) throws SQLException {
    return new Expense(
        new ExpenseIdentifier(rs.getString("id")),
        new Description(rs.getString("description")),
        new Amount(rs.getInt("amount")),
        new PaymentDate(rs.getObject("payment_date", LocalDate.class)),
        new ExpenseAttributeIdentifier(rs.getString("attribute_id")));
  }
}
