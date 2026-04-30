package dev.yhiguchi.home_expense.infrastructure.datasource;

import java.sql.SQLException;

/** データアクセス例外 */
public class DataAccessException extends RuntimeException {

  public DataAccessException(SQLException cause) {
    super(cause);
  }
}
