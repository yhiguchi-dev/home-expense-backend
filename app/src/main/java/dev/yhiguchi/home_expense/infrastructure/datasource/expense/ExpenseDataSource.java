package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

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
        "INSERT INTO expense.expense(id, description, price, payment_date, version) VALUES (?, ?, ?, ?, ?)",
        ps -> {
          ps.setString(1, expense.expenseIdentifier().value());
          ps.setString(2, expense.description().value());
          ps.setInt(3, expense.price().value());
          ps.setDate(4, Date.valueOf(expense.paymentDate().asString()));
          ps.setLong(5, INITIAL_VERSION);
        });
    if (expense.isFixed()) {
      jdbc.update(
          "INSERT INTO expense.fixed_expense(expense_id, attribute_id) VALUES (?, ?)",
          ps -> {
            ps.setString(1, expense.expenseIdentifier().value());
            ps.setString(2, expense.expenseAttributeIdentifier().value());
          });
    }
    if (expense.isVariable()) {
      jdbc.update(
          "INSERT INTO expense.variable_expense(expense_id, attribute_id) VALUES (?, ?)",
          ps -> {
            ps.setString(1, expense.expenseIdentifier().value());
            ps.setString(2, expense.expenseAttributeIdentifier().value());
          });
    }
  }

  @Override
  public Optional<Revision<Expense>> findBy(ExpenseIdentifier expenseIdentifier) {
    String sql =
        """
        SELECT
          expense.id,
          expense.description,
          expense.price,
          expense.payment_date,
          expense.version,
          COALESCE(fixed_expense.attribute_id, variable_expense.attribute_id) AS attribute_id,
          CASE
            WHEN fixed_expense.attribute_id IS NOT NULL THEN '固定費'
            ELSE '変動費'
          END AS category
        FROM expense.expense
        LEFT JOIN expense.fixed_expense
          ON expense.id = fixed_expense.expense_id
        LEFT JOIN expense.variable_expense
          ON expense.id = variable_expense.expense_id
        WHERE expense.id = ?
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
          SELECT 1 FROM expense.fixed_expense WHERE attribute_id = ?
          UNION ALL
          SELECT 1 FROM expense.variable_expense WHERE attribute_id = ?
        ) THEN TRUE ELSE FALSE END
        FROM (VALUES (1)) AS t(x)
        """;
    return jdbc.queryForOptional(
            sql,
            ps -> {
              ps.setString(1, expenseAttributeIdentifier.value());
              ps.setString(2, expenseAttributeIdentifier.value());
            },
            rs -> rs.getBoolean(1))
        .orElse(false);
  }

  @Override
  public void update(Revision<Expense> versioned) {
    Expense expense = versioned.entity();
    jdbc.updateWithOptimisticLock(
        """
        UPDATE expense.expense
        SET description = ?, price = ?, payment_date = ?, version = version + 1
        WHERE id = ? AND version = ?
        """,
        ps -> {
          ps.setString(1, expense.description().value());
          ps.setInt(2, expense.price().value());
          ps.setDate(3, Date.valueOf(expense.paymentDate().asString()));
          ps.setString(4, expense.expenseIdentifier().value());
          ps.setLong(5, versioned.version());
        });
    jdbc.update(
        "DELETE FROM expense.fixed_expense WHERE expense_id = ?",
        ps -> ps.setString(1, expense.expenseIdentifier().value()));
    jdbc.update(
        "DELETE FROM expense.variable_expense WHERE expense_id = ?",
        ps -> ps.setString(1, expense.expenseIdentifier().value()));
    if (expense.isFixed()) {
      jdbc.update(
          "INSERT INTO expense.fixed_expense(expense_id, attribute_id) VALUES (?, ?)",
          ps -> {
            ps.setString(1, expense.expenseIdentifier().value());
            ps.setString(2, expense.expenseAttributeIdentifier().value());
          });
    }
    if (expense.isVariable()) {
      jdbc.update(
          "INSERT INTO expense.variable_expense(expense_id, attribute_id) VALUES (?, ?)",
          ps -> {
            ps.setString(1, expense.expenseIdentifier().value());
            ps.setString(2, expense.expenseAttributeIdentifier().value());
          });
    }
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
        new Price(rs.getInt("price")),
        new PaymentDate(rs.getObject("payment_date", LocalDate.class)),
        new ExpenseAttributeIdentifier(rs.getString("attribute_id")),
        ExpenseCategory.valueOf(rs.getString("category")));
  }

  static Revision<Expense> mapRevisionExpense(ResultSet rs) throws SQLException {
    return new Revision<>(mapExpense(rs), rs.getLong("version"));
  }
}
