package dev.yhiguchi.home_expense.infrastructure.datasource.jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@FunctionalInterface
public interface ParameterBinder {
  void bind(PreparedStatement ps) throws SQLException;

  ParameterBinder EMPTY = ps -> {};

  static ParameterBinder of(List<Object> params) {
    return ps -> {
      for (int i = 0; i < params.size(); i++) {
        Object param = params.get(i);
        int index = i + 1;
        switch (param) {
          case Integer intVal -> ps.setInt(index, intVal);
          case Long longVal -> ps.setLong(index, longVal);
          case String strVal -> ps.setString(index, strVal);
          case Date dateVal -> ps.setDate(index, dateVal);
          case null -> ps.setObject(index, null);
          default -> ps.setObject(index, param);
        }
      }
    };
  }
}
