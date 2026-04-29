package dev.yhiguchi.home_expense.infrastructure.datasource.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import dev.yhiguchi.home_expense.infrastructure.datasource.IntegrityConstraintViolationException;
import dev.yhiguchi.home_expense.infrastructure.datasource.UniqueConstraintViolationException;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class SqlStateExceptionTranslatorTest {

  private final SqlStateExceptionTranslator sut = new SqlStateExceptionTranslator();

  @Test
  void SQLSTATE_23505はUniqueConstraintViolationExceptionに翻訳される() {
    SQLException cause = new SQLException("duplicate key", "23505");

    DataAccessException result = sut.translate(cause);

    assertInstanceOf(UniqueConstraintViolationException.class, result);
    assertSame(cause, result.getCause());
  }

  @Test
  void クラス23の未知サブクラスはIntegrityConstraintViolationExceptionに翻訳される() {
    SQLException cause = new SQLException("foreign key violation", "23503");

    DataAccessException result = sut.translate(cause);

    assertInstanceOf(IntegrityConstraintViolationException.class, result);
    assertFalse(result instanceof UniqueConstraintViolationException);
  }

  @Test
  void クラス23以外はDataAccessExceptionに翻訳される() {
    SQLException cause = new SQLException("syntax error", "42601");

    DataAccessException result = sut.translate(cause);

    assertEquals(DataAccessException.class, result.getClass());
  }

  @Test
  void SQLSTATEがnullの場合はDataAccessExceptionに翻訳される() {
    SQLException cause = new SQLException("connection lost");

    DataAccessException result = sut.translate(cause);

    assertEquals(DataAccessException.class, result.getClass());
    assertSame(cause, result.getCause());
  }
}
