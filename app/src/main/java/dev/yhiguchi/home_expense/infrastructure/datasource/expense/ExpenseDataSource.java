package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.infrastructure.datasource.ConcurrentUpdateException;
import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import javax.sql.DataSource;

@ApplicationScoped
public class ExpenseDataSource implements ExpenseRepository {

  DataSource dataSource;

  public ExpenseDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void register(Expense expense) {
    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement ps =
          conn.prepareStatement(
              "INSERT INTO expense.expense(id, description, price, payment_date, version) VALUES (?, ?, ?, ?, ?)")) {
        ps.setString(1, expense.expenseIdentifier().value());
        ps.setString(2, expense.description().value());
        ps.setInt(3, expense.price().value());
        ps.setDate(4, Date.valueOf(expense.paymentDate().asString()));
        ps.setLong(5, expense.version());
        ps.executeUpdate();
      }
      if (expense.isFixed()) {
        try (PreparedStatement ps =
            conn.prepareStatement(
                "INSERT INTO expense.fixed_expense(expense_id, attribute_id) VALUES (?, ?)")) {
          ps.setString(1, expense.expenseIdentifier().value());
          ps.setString(2, expense.expenseAttributeIdentifier().value());
          ps.executeUpdate();
        }
      }
      if (expense.isVariable()) {
        try (PreparedStatement ps =
            conn.prepareStatement(
                "INSERT INTO expense.variable_expense(expense_id, attribute_id) VALUES (?, ?)")) {
          ps.setString(1, expense.expenseIdentifier().value());
          ps.setString(2, expense.expenseAttributeIdentifier().value());
          ps.executeUpdate();
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public Optional<Expense> findBy(ExpenseIdentifier expenseIdentifier) {
    return selectBy(expenseIdentifier);
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
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, expenseAttributeIdentifier.value());
      ps.setString(2, expenseAttributeIdentifier.value());
      try (ResultSet rs = ps.executeQuery()) {
        rs.next();
        return rs.getBoolean(1);
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void update(Expense expense) {
    try (Connection conn = dataSource.getConnection()) {
      String sql =
          """
          UPDATE expense.expense
          SET description = ?, price = ?, payment_date = ?, version = version + 1
          WHERE id = ? AND version = ?
          """;
      try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, expense.description().value());
        ps.setInt(2, expense.price().value());
        ps.setDate(3, Date.valueOf(expense.paymentDate().asString()));
        ps.setString(4, expense.expenseIdentifier().value());
        ps.setLong(5, expense.version());
        int rowCount = ps.executeUpdate();
        if (rowCount == 0) {
          throw new ConcurrentUpdateException();
        }
      }
      try (PreparedStatement ps =
          conn.prepareStatement("DELETE FROM expense.fixed_expense WHERE expense_id = ?")) {
        ps.setString(1, expense.expenseIdentifier().value());
        ps.executeUpdate();
      }
      try (PreparedStatement ps =
          conn.prepareStatement("DELETE FROM expense.variable_expense WHERE expense_id = ?")) {
        ps.setString(1, expense.expenseIdentifier().value());
        ps.executeUpdate();
      }
      if (expense.isFixed()) {
        try (PreparedStatement ps =
            conn.prepareStatement(
                "INSERT INTO expense.fixed_expense(expense_id, attribute_id) VALUES (?, ?)")) {
          ps.setString(1, expense.expenseIdentifier().value());
          ps.setString(2, expense.expenseAttributeIdentifier().value());
          ps.executeUpdate();
        }
      }
      if (expense.isVariable()) {
        try (PreparedStatement ps =
            conn.prepareStatement(
                "INSERT INTO expense.variable_expense(expense_id, attribute_id) VALUES (?, ?)")) {
          ps.setString(1, expense.expenseIdentifier().value());
          ps.setString(2, expense.expenseAttributeIdentifier().value());
          ps.executeUpdate();
        }
      }
    } catch (ConcurrentUpdateException e) {
      throw e;
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void delete(Expense expense) {
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps =
            conn.prepareStatement("DELETE FROM expense.expense WHERE expense.id = ?")) {
      ps.setString(1, expense.expenseIdentifier().value());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  private Optional<Expense> selectBy(ExpenseIdentifier expenseIdentifier) {
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
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, expenseIdentifier.value());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapExpense(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  static Expense mapExpense(ResultSet rs) throws SQLException {
    return new Expense(
        new ExpenseIdentifier(rs.getString("id")),
        new Description(rs.getString("description")),
        new Price(rs.getInt("price")),
        new PaymentDate(rs.getObject("payment_date", LocalDate.class)),
        new ExpenseAttributeIdentifier(rs.getString("attribute_id")),
        ExpenseCategory.valueOf(rs.getString("category")),
        rs.getLong("version"));
  }
}
