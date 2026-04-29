package dev.yhiguchi.home_expense.infrastructure.datasource.jdbc;

import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import java.sql.SQLException;

/**
 * JDBC の {@link SQLException} をドメイン中立な {@link DataAccessException} 階層に翻訳する戦略。
 *
 * <p>既定実装は ISO/IEC 9075 SQLSTATE に準拠したベンダー非依存の判定を行う。
 * 異なる規約を持つ DB(MySQL 等)に移行する際は本インタフェースの別実装を差し替える。
 */
public interface SqlExceptionTranslator {

  DataAccessException translate(SQLException cause);
}
