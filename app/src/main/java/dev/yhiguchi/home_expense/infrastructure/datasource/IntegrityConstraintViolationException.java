package dev.yhiguchi.home_expense.infrastructure.datasource;

import java.sql.SQLException;

/** ISO/IEC 9075 SQLSTATE class 23 (integrity_constraint_violation) を示す例外。 */
public class IntegrityConstraintViolationException extends DataAccessException {

  public IntegrityConstraintViolationException(SQLException cause) {
    super(cause);
  }
}
