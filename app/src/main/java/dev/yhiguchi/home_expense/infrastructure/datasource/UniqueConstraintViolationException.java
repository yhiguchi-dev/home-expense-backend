package dev.yhiguchi.home_expense.infrastructure.datasource;

import java.sql.SQLException;

/** ISO/IEC 9075 SQLSTATE 23505 (unique_violation) を示す例外。 */
public class UniqueConstraintViolationException extends DataAccessException {

  public UniqueConstraintViolationException(SQLException cause) {
    super(cause);
  }
}
