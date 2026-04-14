package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.ConcurrentUpdateException;
import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        ps.setDate(4, Date.valueOf(expense.paymentDate().value()));
        ps.setLong(5, expense.version());
        ps.executeUpdate();
      }
      if (expense.isFixed()) {
        try (PreparedStatement ps =
            conn.prepareStatement(
                "INSERT INTO expense.fixed_expense(expense_id, attribute_id) VALUES (?, ?)")) {
          ps.setString(1, expense.expenseIdentifier().value());
          ps.setString(2, expense.expenseAttribute().expenseAttributeIdentifier().value());
          ps.executeUpdate();
        }
      }
      if (expense.isVariable()) {
        try (PreparedStatement ps =
            conn.prepareStatement(
                "INSERT INTO expense.variable_expense(expense_id, attribute_id) VALUES (?, ?)")) {
          ps.setString(1, expense.expenseIdentifier().value());
          ps.setString(2, expense.expenseAttribute().expenseAttributeIdentifier().value());
          ps.executeUpdate();
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public Expense get(ExpenseIdentifier expenseIdentifier) {
    Optional<Expense> expense = selectBy(expenseIdentifier);
    return expense.orElseThrow(ExpenseNotFoundException::new);
  }

  @Override
  public Expenses find(ExpenseAttribute expenseAttribute) {
    String sql =
        """
        SELECT
          expense.id,
          expense.description,
          expense.price,
          expense.payment_date,
          expense.version,
          attribute.id AS attribute_id,
          attribute.category,
          attribute.name AS attribute_name
        FROM expense.expense
        LEFT JOIN fixed_expense
          ON expense.id = fixed_expense.expense_id
        LEFT JOIN variable_expense
          ON expense.id = variable_expense.expense_id
        LEFT JOIN attribute
          ON attribute.id = fixed_expense.attribute_id
            OR attribute.id = variable_expense.attribute_id
        WHERE attribute.id = ?
        """;
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, expenseAttribute.expenseAttributeIdentifier().value());
      try (ResultSet rs = ps.executeQuery()) {
        List<Expense> list = new ArrayList<>();
        while (rs.next()) {
          list.add(mapExpense(rs));
        }
        if (list.isEmpty()) {
          return new Expenses();
        }
        return new Expenses(list);
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
        ps.setDate(3, Date.valueOf(expense.paymentDate().value()));
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
          ps.setString(2, expense.expenseAttribute().expenseAttributeIdentifier().value());
          ps.executeUpdate();
        }
      }
      if (expense.isVariable()) {
        try (PreparedStatement ps =
            conn.prepareStatement(
                "INSERT INTO expense.variable_expense(expense_id, attribute_id) VALUES (?, ?)")) {
          ps.setString(1, expense.expenseIdentifier().value());
          ps.setString(2, expense.expenseAttribute().expenseAttributeIdentifier().value());
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
  public void delete(ExpenseIdentifier expenseIdentifier) {
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps =
            conn.prepareStatement("DELETE FROM expense.expense WHERE expense.id = ?")) {
      ps.setString(1, expenseIdentifier.value());
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
          attribute.id AS attribute_id,
          attribute.category,
          attribute.name AS attribute_name
        FROM expense.expense
        LEFT JOIN fixed_expense
          ON expense.id = fixed_expense.expense_id
        LEFT JOIN variable_expense
          ON expense.id = variable_expense.expense_id
        JOIN attribute
          ON attribute.id = fixed_expense.attribute_id
            OR attribute.id = variable_expense.attribute_id
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
    ExpenseAttribute expenseAttribute =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier(rs.getString("attribute_id")),
            new ExpenseAttributeName(rs.getString("attribute_name")),
            ExpenseCategory.valueOf(rs.getString("category")));
    return new Expense(
        new ExpenseIdentifier(rs.getString("id")),
        new Description(rs.getString("description")),
        new Price(rs.getInt("price")),
        new PaymentDate(rs.getObject("payment_date", LocalDate.class)),
        expenseAttribute,
        rs.getLong("version"));
  }
}
