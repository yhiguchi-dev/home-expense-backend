package dev.yhiguchi.home_expense.infrastructure.datasource.jdbc;

import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import dev.yhiguchi.home_expense.infrastructure.datasource.IntegrityConstraintViolationException;
import dev.yhiguchi.home_expense.infrastructure.datasource.UniqueConstraintViolationException;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.SQLException;

/**
 * ISO/IEC 9075 SQLSTATE に準拠する DB(PostgreSQL, CockroachDB, DB2, H2, HSQLDB 等)向けの既定翻訳器。
 *
 * <p>クラス(先頭2桁)で大分類を、サブクラスを含む完全一致で個別の例外型を決定する。
 * 未知のサブクラスでもクラスが一致すれば対応する上位型に翻訳する。
 */
@ApplicationScoped
public class SqlStateExceptionTranslator implements SqlExceptionTranslator {

  private static final String CLASS_INTEGRITY_CONSTRAINT_VIOLATION = "23";
  private static final String UNIQUE_VIOLATION = "23505";

  @Override
  public DataAccessException translate(SQLException cause) {
    String state = cause.getSQLState();
    if (state == null) {
      return new DataAccessException(cause);
    }
    if (UNIQUE_VIOLATION.equals(state)) {
      return new UniqueConstraintViolationException(cause);
    }
    if (state.startsWith(CLASS_INTEGRITY_CONSTRAINT_VIOLATION)) {
      return new IntegrityConstraintViolationException(cause);
    }
    return new DataAccessException(cause);
  }
}
