package dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.OptimisticLockException;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.Optional;
import javax.sql.DataSource;

@ApplicationScoped
public class ExpenseAttributeDataSource implements ExpenseAttributeRepository {

  DataSource dataSource;

  public ExpenseAttributeDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void register(ExpenseAttribute expenseAttribute) {
    String sql = "INSERT INTO expense.attribute(id, category, name, version) VALUES (?, ?, ?, ?)";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, expenseAttribute.expenseAttributeIdentifier().value());
      ps.setString(2, expenseAttribute.expenseCategory().name());
      ps.setString(3, expenseAttribute.expenseAttributeName().value());
      ps.setLong(4, expenseAttribute.version());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public ExpenseAttribute get(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    Optional<ExpenseAttribute> expenseAttribute = selectBy(expenseAttributeIdentifier);
    return expenseAttribute.orElseThrow(ExpenseAttributeNotFoundException::new);
  }

  @Override
  public Optional<ExpenseAttribute> find(ExpenseAttributeName expenseAttributeName) {
    String sql = "SELECT id, category, name, version FROM expense.attribute WHERE name = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, expenseAttributeName.value());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapExpenseAttribute(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void update(ExpenseAttribute expenseAttribute) {
    String sql =
        "UPDATE expense.attribute SET category = ?, name = ?, version = version + 1 WHERE id = ? AND version = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, expenseAttribute.expenseCategory().name());
      ps.setString(2, expenseAttribute.expenseAttributeName().value());
      ps.setString(3, expenseAttribute.expenseAttributeIdentifier().value());
      ps.setLong(4, expenseAttribute.version());
      int rowCount = ps.executeUpdate();
      if (rowCount == 0) {
        throw new OptimisticLockException();
      }
    } catch (OptimisticLockException e) {
      throw e;
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void delete(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    String sql = "DELETE FROM expense.attribute WHERE id = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, expenseAttributeIdentifier.value());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  private Optional<ExpenseAttribute> selectBy(
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    String sql = "SELECT id, category, name, version FROM expense.attribute WHERE id = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, expenseAttributeIdentifier.value());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapExpenseAttribute(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  static ExpenseAttribute mapExpenseAttribute(ResultSet rs) throws SQLException {
    return new ExpenseAttribute(
        new ExpenseAttributeIdentifier(rs.getString("id")),
        new ExpenseAttributeName(rs.getString("name")),
        ExpenseCategory.valueOf(rs.getString("category")),
        rs.getLong("version"));
  }
}
