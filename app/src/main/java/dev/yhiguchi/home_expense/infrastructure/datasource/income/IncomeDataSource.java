package dev.yhiguchi.home_expense.infrastructure.datasource.income;

import dev.yhiguchi.home_expense.domain.model.ConcurrentUpdateException;
import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

@ApplicationScoped
public class IncomeDataSource implements IncomeRepository {

  DataSource dataSource;

  public IncomeDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void register(Income income) {
    String sql =
        "INSERT INTO expense.income(id, attribute_id, description, amount, receive_date, version) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, income.incomeIdentifier().value());
      ps.setString(2, income.incomeAttribute().incomeAttributeIdentifier().value());
      ps.setString(3, income.description().value());
      ps.setInt(4, income.amount().value());
      ps.setDate(5, Date.valueOf(income.receiveDate().value()));
      ps.setLong(6, income.version());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void update(Income income) {
    String sql =
        """
        UPDATE expense.income
        SET attribute_id = ?, description = ?, amount = ?, receive_date = ?, version = version + 1
        WHERE id = ? AND version = ?
        """;
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, income.incomeAttribute().incomeAttributeIdentifier().value());
      ps.setString(2, income.description().value());
      ps.setInt(3, income.amount().value());
      ps.setDate(4, Date.valueOf(income.receiveDate().value()));
      ps.setString(5, income.incomeIdentifier().value());
      ps.setLong(6, income.version());
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
  public void delete(Income income) {
    String sql = "DELETE FROM expense.income WHERE income.id = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, income.incomeIdentifier().value());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public Income get(IncomeIdentifier incomeIdentifier) {
    return selectBy(incomeIdentifier).orElseThrow(IncomeNotFoundException::new);
  }

  @Override
  public Incomes find(IncomeAttribute incomeAttribute) {
    String sql =
        """
        SELECT
          income.id,
          income.description,
          income.amount,
          income.receive_date,
          income.version,
          income_attribute.id AS attribute_id,
          income_attribute.name AS attribute_name
        FROM expense.income
        JOIN expense.income_attribute
          ON income_attribute.id = income.attribute_id
        WHERE income_attribute.id = ?
        """;
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, incomeAttribute.incomeAttributeIdentifier().value());
      try (ResultSet rs = ps.executeQuery()) {
        List<Income> list = new ArrayList<>();
        while (rs.next()) {
          list.add(mapIncome(rs));
        }
        if (list.isEmpty()) {
          return new Incomes();
        }
        return new Incomes(list);
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  private Optional<Income> selectBy(IncomeIdentifier incomeIdentifier) {
    String sql =
        """
        SELECT
          income.id,
          income.description,
          income.amount,
          income.receive_date,
          income.version,
          income_attribute.id AS attribute_id,
          income_attribute.name AS attribute_name
        FROM expense.income
        JOIN expense.income_attribute
          ON income_attribute.id = income.attribute_id
        WHERE income.id = ?
        """;
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, incomeIdentifier.value());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapIncome(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  static Income mapIncome(ResultSet rs) throws SQLException {
    IncomeAttribute incomeAttribute =
        new IncomeAttribute(
            new IncomeAttributeIdentifier(rs.getString("attribute_id")),
            new IncomeAttributeName(rs.getString("attribute_name")));
    return new Income(
        new IncomeIdentifier(rs.getString("id")),
        new Description(rs.getString("description")),
        new Amount(rs.getInt("amount")),
        new ReceiveDate(rs.getObject("receive_date", LocalDate.class)),
        incomeAttribute,
        rs.getLong("version"));
  }
}
