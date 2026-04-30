package dev.yhiguchi.home_expense.infrastructure.datasource.jdbc;

import dev.yhiguchi.home_expense.infrastructure.datasource.ReadOnly;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import javax.sql.DataSource;

/** {@link JdbcOperator} の CDI プロデューサー。default と {@link ReadOnly} の 2 種を提供する。 */
@ApplicationScoped
public class JdbcOperators {

  @Produces
  @ApplicationScoped
  public JdbcOperator primary(DataSource dataSource) {
    return new JdbcOperator(dataSource);
  }

  @Produces
  @ApplicationScoped
  @ReadOnly
  public JdbcOperator readonly(@io.quarkus.agroal.DataSource("readonly") DataSource dataSource) {
    return new JdbcOperator(dataSource);
  }
}
