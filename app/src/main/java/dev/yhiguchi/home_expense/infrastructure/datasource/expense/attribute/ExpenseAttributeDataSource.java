package dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.UniqueConstraintViolationException;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.ParameterBinder;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@ApplicationScoped
public class ExpenseAttributeDataSource implements ExpenseAttributeRepository {

  private static final long INITIAL_VERSION = 1L;

  private final JdbcOperator jdbc;

  public ExpenseAttributeDataSource(JdbcOperator jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public void register(ExpenseAttribute expenseAttribute) {
    try {
      jdbc.update(
          "INSERT INTO expense.attribute(id, category, name, version) VALUES (?, ?, ?, ?)",
          ParameterBinder.of(
              expenseAttribute.expenseAttributeIdentifier().value(),
              expenseAttribute.expenseCategory().name(),
              expenseAttribute.expenseAttributeName().value(),
              INITIAL_VERSION));
    } catch (UniqueConstraintViolationException e) {
      throw new ExpenseAttributeAlreadyExistsException();
    }
  }

  @Override
  public Optional<ExpenseAttribute> find(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    return jdbc.queryForOptional(
        "SELECT id, category, name FROM expense.attribute WHERE id = ?",
        ParameterBinder.of(expenseAttributeIdentifier.value()),
        ExpenseAttributeDataSource::mapExpenseAttribute);
  }

  @Override
  public boolean existsByName(
      ExpenseAttributeName expenseAttributeName, ExpenseCategory expenseCategory) {
    String sql =
        """
        SELECT CASE WHEN EXISTS(
            SELECT 1 FROM expense.attribute WHERE name = ? AND category = ?)
          THEN TRUE ELSE FALSE END
        FROM (VALUES (1)) AS t(x)
        """;
    return jdbc.queryForOptional(
            sql,
            ParameterBinder.of(expenseAttributeName.value(), expenseCategory.name()),
            rs -> rs.getBoolean(1))
        .orElse(false);
  }

  @Override
  public void update(ExpenseAttribute expenseAttribute, long expectedVersion) {
    jdbc.updateWithOptimisticLock(
        "UPDATE expense.attribute SET category = ?, name = ?, version = version + 1 WHERE id = ? AND version = ?",
        ParameterBinder.of(
            expenseAttribute.expenseCategory().name(),
            expenseAttribute.expenseAttributeName().value(),
            expenseAttribute.expenseAttributeIdentifier().value(),
            expectedVersion));
  }

  @Override
  public void delete(ExpenseAttribute expenseAttribute) {
    jdbc.update(
        "DELETE FROM expense.attribute WHERE id = ?",
        ParameterBinder.of(expenseAttribute.expenseAttributeIdentifier().value()));
  }

  static ExpenseAttribute mapExpenseAttribute(ResultSet rs) throws SQLException {
    return new ExpenseAttribute(
        new ExpenseAttributeIdentifier(rs.getString("id")),
        new ExpenseAttributeName(rs.getString("name")),
        ExpenseCategory.valueOf(rs.getString("category")));
  }
}
