package dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;

@ApplicationScoped
public class ExpenseAttributeDataSource implements ExpenseAttributeRepository {

  private static final String UNIQUE_VIOLATION = "23505";
  private static final long INITIAL_VERSION = 1L;

  private final JdbcOperator jdbc;

  public ExpenseAttributeDataSource(DataSource dataSource) {
    this.jdbc = new JdbcOperator(dataSource);
  }

  @Override
  public void register(ExpenseAttribute expenseAttribute) {
    try {
      jdbc.update(
          "INSERT INTO expense.attribute(id, category, name, version) VALUES (?, ?, ?, ?)",
          ps -> {
            ps.setString(1, expenseAttribute.expenseAttributeIdentifier().value());
            ps.setString(2, expenseAttribute.expenseCategory().name());
            ps.setString(3, expenseAttribute.expenseAttributeName().value());
            ps.setLong(4, INITIAL_VERSION);
          });
    } catch (DataAccessException e) {
      if (e.getCause() instanceof SQLException sqlEx
          && UNIQUE_VIOLATION.equals(sqlEx.getSQLState())) {
        throw new ExpenseAttributeAlreadyExistsException();
      }
      throw e;
    }
  }

  @Override
  public Optional<Revision<ExpenseAttribute>> findBy(
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    return jdbc.queryForOptional(
        "SELECT id, category, name, version FROM expense.attribute WHERE id = ?",
        ps -> ps.setString(1, expenseAttributeIdentifier.value()),
        ExpenseAttributeDataSource::mapRevisionExpenseAttribute);
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
            ps -> {
              ps.setString(1, expenseAttributeName.value());
              ps.setString(2, expenseCategory.name());
            },
            rs -> rs.getBoolean(1))
        .orElse(false);
  }

  @Override
  public void update(Revision<ExpenseAttribute> versioned) {
    ExpenseAttribute expenseAttribute = versioned.entity();
    jdbc.updateWithOptimisticLock(
        "UPDATE expense.attribute SET category = ?, name = ?, version = version + 1 WHERE id = ? AND version = ?",
        ps -> {
          ps.setString(1, expenseAttribute.expenseCategory().name());
          ps.setString(2, expenseAttribute.expenseAttributeName().value());
          ps.setString(3, expenseAttribute.expenseAttributeIdentifier().value());
          ps.setLong(4, versioned.version());
        });
  }

  @Override
  public void delete(ExpenseAttribute expenseAttribute) {
    jdbc.update(
        "DELETE FROM expense.attribute WHERE id = ?",
        ps -> ps.setString(1, expenseAttribute.expenseAttributeIdentifier().value()));
  }

  static ExpenseAttribute mapExpenseAttribute(ResultSet rs) throws SQLException {
    return new ExpenseAttribute(
        new ExpenseAttributeIdentifier(rs.getString("id")),
        new ExpenseAttributeName(rs.getString("name")),
        ExpenseCategory.valueOf(rs.getString("category")));
  }

  static Revision<ExpenseAttribute> mapRevisionExpenseAttribute(ResultSet rs) throws SQLException {
    return new Revision<>(mapExpenseAttribute(rs), rs.getLong("version"));
  }
}
