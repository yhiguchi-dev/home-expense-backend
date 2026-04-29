package dev.yhiguchi.home_expense.infrastructure.datasource.income;

import dev.yhiguchi.home_expense.domain.model.Amount;
import dev.yhiguchi.home_expense.domain.model.Description;
import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.ParameterBinder;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
public class IncomeDataSource implements IncomeRepository {

  private static final long INITIAL_VERSION = 1L;

  private final JdbcOperator jdbc;

  public IncomeDataSource(JdbcOperator jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public void register(Income income) {
    jdbc.update(
        "INSERT INTO expense.income(id, attribute_id, description, amount, receive_date, version) VALUES (?, ?, ?, ?, ?, ?)",
        ParameterBinder.of(
            income.incomeIdentifier().value(),
            income.incomeAttributeIdentifier().value(),
            income.description().value(),
            income.amount().value(),
            income.receiveDate().value(),
            INITIAL_VERSION));
  }

  @Override
  public void update(Income income, long expectedVersion) {
    jdbc.updateWithOptimisticLock(
        """
        UPDATE expense.income
        SET attribute_id = ?, description = ?, amount = ?, receive_date = ?, version = version + 1
        WHERE id = ? AND version = ?
        """,
        ParameterBinder.of(
            income.incomeAttributeIdentifier().value(),
            income.description().value(),
            income.amount().value(),
            income.receiveDate().value(),
            income.incomeIdentifier().value(),
            expectedVersion));
  }

  @Override
  public void delete(Income income) {
    jdbc.update(
        "DELETE FROM expense.income WHERE income.id = ?",
        ParameterBinder.of(income.incomeIdentifier().value()));
  }

  @Override
  public Optional<Income> find(IncomeIdentifier incomeIdentifier) {
    return jdbc.queryForOptional(
        """
        SELECT id, attribute_id, description, amount, receive_date
        FROM expense.income
        WHERE id = ?
        """,
        ParameterBinder.of(incomeIdentifier.value()),
        IncomeDataSource::mapIncome);
  }

  @Override
  public boolean existsByAttributeIdentifier(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    String sql =
        """
        SELECT CASE WHEN EXISTS(SELECT 1 FROM expense.income WHERE attribute_id = ?)
          THEN TRUE ELSE FALSE END
        FROM (VALUES (1)) AS t(x)
        """;
    return jdbc.queryForOptional(
            sql, ParameterBinder.of(incomeAttributeIdentifier.value()), rs -> rs.getBoolean(1))
        .orElse(false);
  }

  static Income mapIncome(ResultSet rs) throws SQLException {
    return new Income(
        new IncomeIdentifier(rs.getString("id")),
        new Description(rs.getString("description")),
        new Amount(rs.getInt("amount")),
        new ReceiveDate(rs.getObject("receive_date", LocalDate.class)),
        new IncomeAttributeIdentifier(rs.getString("attribute_id")));
  }
}
