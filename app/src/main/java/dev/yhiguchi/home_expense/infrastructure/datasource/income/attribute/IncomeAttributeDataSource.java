package dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute;

import dev.yhiguchi.home_expense.domain.model.ConcurrentUpdateException;
import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.Optional;
import javax.sql.DataSource;

@ApplicationScoped
public class IncomeAttributeDataSource implements IncomeAttributeRepository {

  DataSource dataSource;

  public IncomeAttributeDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  private static final String UNIQUE_VIOLATION = "23505";

  @Override
  public void register(IncomeAttribute incomeAttribute) {
    String sql = "INSERT INTO expense.income_attribute(id, name, version) VALUES (?, ?, ?)";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, incomeAttribute.incomeAttributeIdentifier().value());
      ps.setString(2, incomeAttribute.incomeAttributeName().value());
      ps.setLong(3, incomeAttribute.version());
      ps.executeUpdate();
    } catch (SQLException e) {
      if (UNIQUE_VIOLATION.equals(e.getSQLState())) {
        throw new IncomeAttributeAlreadyExistsException();
      }
      throw new DataAccessException(e);
    }
  }

  @Override
  public void update(IncomeAttribute incomeAttribute) {
    String sql =
        "UPDATE expense.income_attribute SET name = ?, version = version + 1 WHERE id = ? AND version = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, incomeAttribute.incomeAttributeName().value());
      ps.setString(2, incomeAttribute.incomeAttributeIdentifier().value());
      ps.setLong(3, incomeAttribute.version());
      int rowCount = ps.executeUpdate();
      if (rowCount == 0) {
        throw new ConcurrentUpdateException();
      }
    } catch (ConcurrentUpdateException e) {
      throw e;
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void delete(IncomeAttribute incomeAttribute) {
    String sql = "DELETE FROM expense.income_attribute WHERE id = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, incomeAttribute.incomeAttributeIdentifier().value());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public IncomeAttribute get(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    Optional<IncomeAttribute> incomeAttribute = selectBy(incomeAttributeIdentifier);
    return incomeAttribute.orElseThrow(IncomeAttributeNotFoundException::new);
  }

  @Override
  public boolean existsByName(IncomeAttributeName incomeAttributeName) {
    String sql = "SELECT EXISTS(SELECT 1 FROM expense.income_attribute WHERE name = ?)";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, incomeAttributeName.value());
      try (ResultSet rs = ps.executeQuery()) {
        rs.next();
        return rs.getBoolean(1);
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  private Optional<IncomeAttribute> selectBy(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    String sql = "SELECT id, name, version FROM expense.income_attribute WHERE id = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, incomeAttributeIdentifier.value());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapIncomeAttribute(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  static IncomeAttribute mapIncomeAttribute(ResultSet rs) throws SQLException {
    return new IncomeAttribute(
        new IncomeAttributeIdentifier(rs.getString("id")),
        new IncomeAttributeName(rs.getString("name")),
        rs.getLong("version"));
  }
}
