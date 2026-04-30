package dev.yhiguchi.home_expense.infrastructure.datasource.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {
  T map(ResultSet rs) throws SQLException;
}
