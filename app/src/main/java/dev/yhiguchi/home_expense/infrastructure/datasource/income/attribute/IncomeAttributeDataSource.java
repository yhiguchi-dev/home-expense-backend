package dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;

@ApplicationScoped
public class IncomeAttributeDataSource implements IncomeAttributeRepository {

  private static final String UNIQUE_VIOLATION = "23505";
  private static final long INITIAL_VERSION = 1L;

  private final JdbcOperator jdbc;

  public IncomeAttributeDataSource(DataSource dataSource) {
    this.jdbc = new JdbcOperator(dataSource);
  }

  @Override
  public void register(IncomeAttribute incomeAttribute) {
    try {
      jdbc.update(
          "INSERT INTO expense.income_attribute(id, name, version) VALUES (?, ?, ?)",
          ps -> {
            ps.setString(1, incomeAttribute.incomeAttributeIdentifier().value());
            ps.setString(2, incomeAttribute.incomeAttributeName().value());
            ps.setLong(3, INITIAL_VERSION);
          });
    } catch (DataAccessException e) {
      if (e.getCause() instanceof SQLException sqlEx
          && UNIQUE_VIOLATION.equals(sqlEx.getSQLState())) {
        throw new IncomeAttributeAlreadyExistsException();
      }
      throw e;
    }
  }

  @Override
  public void update(Revision<IncomeAttribute> versioned) {
    IncomeAttribute incomeAttribute = versioned.entity();
    jdbc.updateWithOptimisticLock(
        "UPDATE expense.income_attribute SET name = ?, version = version + 1 WHERE id = ? AND version = ?",
        ps -> {
          ps.setString(1, incomeAttribute.incomeAttributeName().value());
          ps.setString(2, incomeAttribute.incomeAttributeIdentifier().value());
          ps.setLong(3, versioned.version());
        });
  }

  @Override
  public void delete(IncomeAttribute incomeAttribute) {
    jdbc.update(
        "DELETE FROM expense.income_attribute WHERE id = ?",
        ps -> ps.setString(1, incomeAttribute.incomeAttributeIdentifier().value()));
  }

  @Override
  public Optional<Revision<IncomeAttribute>> findBy(
      IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return jdbc.queryForOptional(
        "SELECT id, name, version FROM expense.income_attribute WHERE id = ?",
        ps -> ps.setString(1, incomeAttributeIdentifier.value()),
        IncomeAttributeDataSource::mapRevisionIncomeAttribute);
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
            sql, ps -> ps.setString(1, incomeAttributeName.value()), rs -> rs.getBoolean(1))
        .orElse(false);
  }

  static IncomeAttribute mapIncomeAttribute(ResultSet rs) throws SQLException {
    return new IncomeAttribute(
        new IncomeAttributeIdentifier(rs.getString("id")),
        new IncomeAttributeName(rs.getString("name")));
  }

  static Revision<IncomeAttribute> mapRevisionIncomeAttribute(ResultSet rs) throws SQLException {
    return new Revision<>(mapIncomeAttribute(rs), rs.getLong("version"));
  }
}
