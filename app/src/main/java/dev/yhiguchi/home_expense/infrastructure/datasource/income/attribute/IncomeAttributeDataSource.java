package dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute;

import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.UniqueConstraintViolationException;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.ParameterBinder;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@ApplicationScoped
public class IncomeAttributeDataSource implements IncomeAttributeRepository {

  private static final long INITIAL_VERSION = 1L;

  private final JdbcOperator jdbc;

  public IncomeAttributeDataSource(JdbcOperator jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public void register(IncomeAttribute incomeAttribute) {
    try {
      jdbc.update(
          "INSERT INTO expense.income_attribute(id, name, version) VALUES (?, ?, ?)",
          ParameterBinder.of(
              incomeAttribute.incomeAttributeIdentifier().value(),
              incomeAttribute.incomeAttributeName().value(),
              INITIAL_VERSION));
    } catch (UniqueConstraintViolationException e) {
      throw new IncomeAttributeAlreadyExistsException();
    }
  }

  @Override
  public void update(IncomeAttribute incomeAttribute, long expectedVersion) {
    jdbc.updateWithOptimisticLock(
        "UPDATE expense.income_attribute SET name = ?, version = version + 1 WHERE id = ? AND version = ?",
        ParameterBinder.of(
            incomeAttribute.incomeAttributeName().value(),
            incomeAttribute.incomeAttributeIdentifier().value(),
            expectedVersion));
  }

  @Override
  public void delete(IncomeAttribute incomeAttribute) {
    jdbc.update(
        "DELETE FROM expense.income_attribute WHERE id = ?",
        ParameterBinder.of(incomeAttribute.incomeAttributeIdentifier().value()));
  }

  @Override
  public Optional<IncomeAttribute> find(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return jdbc.queryForOptional(
        "SELECT id, name FROM expense.income_attribute WHERE id = ?",
        ParameterBinder.of(incomeAttributeIdentifier.value()),
        IncomeAttributeDataSource::mapIncomeAttribute);
  }

  @Override
  public boolean existsByName(IncomeAttributeName incomeAttributeName) {
    String sql =
        """
        SELECT CASE WHEN EXISTS(SELECT 1 FROM expense.income_attribute WHERE name = ?)
          THEN TRUE ELSE FALSE END
        FROM (VALUES (1)) AS t(x)
        """;
    return jdbc.queryForOptional(
            sql, ParameterBinder.of(incomeAttributeName.value()), rs -> rs.getBoolean(1))
        .orElse(false);
  }

  static IncomeAttribute mapIncomeAttribute(ResultSet rs) throws SQLException {
    return new IncomeAttribute(
        new IncomeAttributeIdentifier(rs.getString("id")),
        new IncomeAttributeName(rs.getString("name")));
  }
}
