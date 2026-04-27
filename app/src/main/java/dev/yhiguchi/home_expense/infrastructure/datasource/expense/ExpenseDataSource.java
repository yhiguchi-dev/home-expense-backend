package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.Amount;
import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import javax.sql.DataSource;

@ApplicationScoped
public class ExpenseDataSource implements ExpenseRepository {

  private static final long INITIAL_VERSION = 1L;

  private final JdbcOperator jdbc;

  public ExpenseDataSource(DataSource dataSource) {
    this.jdbc = new JdbcOperator(dataSource);
  }

  @Override
  public void register(Expense expense) {
    jdbc.update(
        "INSERT INTO expense.expense(id, description, amount, payment_date, attribute_id, version)"
            + " VALUES (?, ?, ?, ?, ?, ?)",
        ps -> {
          ps.setString(1, expense.expenseIdentifier().value());
          ps.setString(2, expense.description().value());
          ps.setInt(3, expense.amount().value());
          ps.setDate(4, Date.valueOf(expense.paymentDate().asString()));
          ps.setString(5, expense.expenseAttributeIdentifier().value());
          ps.setLong(6, INITIAL_VERSION);
        });
  }

  @Override
  public Optional<Revision<Expense>> findBy(ExpenseIdentifier expenseIdentifier) {
    String sql =
        """
        SELECT id, description, amount, payment_date, attribute_id, version
        FROM expense.expense
        WHERE id = ?
        """;
    return jdbc.queryForOptional(
        sql,
        ps -> ps.setString(1, expenseIdentifier.value()),
        ExpenseDataSource::mapRevisionExpense);
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
            sql, ps -> ps.setString(1, expenseAttributeIdentifier.value()), rs -> rs.getBoolean(1))
        .orElse(false);
  }

  @Override
  public void update(Revision<Expense> versioned) {
    Expense expense = versioned.entity();
    jdbc.updateWithOptimisticLock(
        """
        UPDATE expense.expense
        SET description = ?, amount = ?, payment_date = ?, attribute_id = ?, version = version + 1
        WHERE id = ? AND version = ?
        """,
        ps -> {
          ps.setString(1, expense.description().value());
          ps.setInt(2, expense.amount().value());
          ps.setDate(3, Date.valueOf(expense.paymentDate().asString()));
          ps.setString(4, expense.expenseAttributeIdentifier().value());
          ps.setString(5, expense.expenseIdentifier().value());
          ps.setLong(6, versioned.version());
        });
  }

  @Override
  public void delete(Expense expense) {
    jdbc.update(
        "DELETE FROM expense.expense WHERE expense.id = ?",
        ps -> ps.setString(1, expense.expenseIdentifier().value()));
  }

  static Expense mapExpense(ResultSet rs) throws SQLException {
    return new Expense(
        new ExpenseIdentifier(rs.getString("id")),
        new Description(rs.getString("description")),
        new Amount(rs.getInt("amount")),
        new PaymentDate(rs.getObject("payment_date", LocalDate.class)),
        new ExpenseAttributeIdentifier(rs.getString("attribute_id")));
  }

  static Revision<Expense> mapRevisionExpense(ResultSet rs) throws SQLException {
    return new Revision<>(mapExpense(rs), rs.getLong("version"));
  }
}
