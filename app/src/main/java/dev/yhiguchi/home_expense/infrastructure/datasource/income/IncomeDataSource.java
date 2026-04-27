package dev.yhiguchi.home_expense.infrastructure.datasource.income;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import javax.sql.DataSource;

@ApplicationScoped
public class IncomeDataSource implements IncomeRepository {

  private static final long INITIAL_VERSION = 1L;

  private final JdbcOperator jdbc;

  public IncomeDataSource(DataSource dataSource) {
    this.jdbc = new JdbcOperator(dataSource);
  }

  @Override
  public void register(Income income) {
    jdbc.update(
        "INSERT INTO expense.income(id, attribute_id, description, amount, receive_date, version) VALUES (?, ?, ?, ?, ?, ?)",
        ps -> {
          ps.setString(1, income.incomeIdentifier().value());
          ps.setString(2, income.incomeAttributeIdentifier().value());
          ps.setString(3, income.description().value());
          ps.setInt(4, income.amount().value());
          ps.setDate(5, Date.valueOf(income.receiveDate().asString()));
          ps.setLong(6, INITIAL_VERSION);
        });
  }

  @Override
  public void update(Revision<Income> versioned) {
    Income income = versioned.entity();
    jdbc.updateWithOptimisticLock(
        """
        UPDATE expense.income
        SET attribute_id = ?, description = ?, amount = ?, receive_date = ?, version = version + 1
        WHERE id = ? AND version = ?
        """,
        ps -> {
          ps.setString(1, income.incomeAttributeIdentifier().value());
          ps.setString(2, income.description().value());
          ps.setInt(3, income.amount().value());
          ps.setDate(4, Date.valueOf(income.receiveDate().asString()));
          ps.setString(5, income.incomeIdentifier().value());
          ps.setLong(6, versioned.version());
        });
  }

  @Override
  public void delete(Income income) {
    jdbc.update(
        "DELETE FROM expense.income WHERE income.id = ?",
        ps -> ps.setString(1, income.incomeIdentifier().value()));
  }

  @Override
  public Optional<Revision<Income>> findBy(IncomeIdentifier incomeIdentifier) {
    return jdbc.queryForOptional(
        """
        SELECT id, attribute_id, description, amount, receive_date, version
        FROM expense.income
        WHERE id = ?
        """,
        ps -> ps.setString(1, incomeIdentifier.value()),
        IncomeDataSource::mapRevisionIncome);
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
            sql, ps -> ps.setString(1, incomeAttributeIdentifier.value()), rs -> rs.getBoolean(1))
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

  static Revision<Income> mapRevisionIncome(ResultSet rs) throws SQLException {
    return new Revision<>(mapIncome(rs), rs.getLong("version"));
  }
}
