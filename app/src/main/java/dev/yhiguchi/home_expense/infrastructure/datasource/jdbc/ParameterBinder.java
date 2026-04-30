package dev.yhiguchi.home_expense.infrastructure.datasource.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ParameterBinder {
  void bind(PreparedStatement ps) throws SQLException;

  ParameterBinder EMPTY = ps -> {};

  static ParameterBinder of(Object... params) {
    return ps -> {
      for (int i = 0; i < params.length; i++) {
        ps.setObject(i + 1, params[i]);
      }
    };
  }
}
