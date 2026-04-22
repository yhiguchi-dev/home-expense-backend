package dev.yhiguchi.home_expense.infrastructure.datasource.jdbc;

import dev.yhiguchi.home_expense.infrastructure.datasource.ConcurrentUpdateException;
import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

public class JdbcOperator {

  private final DataSource dataSource;

  public JdbcOperator(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public <T> Optional<T> queryForOptional(String sql, ParameterBinder binder, RowMapper<T> mapper) {
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      binder.bind(ps);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? Optional.ofNullable(mapper.map(rs)) : Optional.empty();
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  public <T> List<T> queryForList(String sql, ParameterBinder binder, RowMapper<T> mapper) {
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      binder.bind(ps);
      try (ResultSet rs = ps.executeQuery()) {
        List<T> list = new ArrayList<>();
        while (rs.next()) {
          list.add(mapper.map(rs));
        }
        return list;
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  public int update(String sql, ParameterBinder binder) {
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      binder.bind(ps);
      return ps.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  public void updateWithOptimisticLock(String sql, ParameterBinder binder) {
    if (update(sql, binder) == 0) {
      throw new ConcurrentUpdateException();
    }
  }
}
