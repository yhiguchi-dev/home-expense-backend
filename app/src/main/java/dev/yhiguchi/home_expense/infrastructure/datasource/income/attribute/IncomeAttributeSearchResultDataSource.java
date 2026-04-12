package dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchCriteria;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchResult;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchResultQuerier;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

@ApplicationScoped
public class IncomeAttributeSearchResultDataSource implements IncomeAttributeSearchResultQuerier {

  DataSource dataSource;

  public IncomeAttributeSearchResultDataSource(
      @io.quarkus.agroal.DataSource("readonly") DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public IncomeAttributeSearchResult find(IncomeAttributeSearchCriteria criteria) {
    int count = selectCount();
    if (count == 0) {
      return new IncomeAttributeSearchResult();
    }
    List<IncomeAttribute> list = selectBy(criteria);
    return new IncomeAttributeSearchResult(count, list);
  }

  private int selectCount() {
    String sql = "SELECT COUNT(*) FROM expense.income_attribute";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      rs.next();
      return rs.getInt(1);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  private List<IncomeAttribute> selectBy(IncomeAttributeSearchCriteria criteria) {
    String sql =
        "SELECT id, name, version FROM expense.income_attribute ORDER BY income_attribute.created_at OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, criteria.offset());
      ps.setInt(2, criteria.perPage());
      try (ResultSet rs = ps.executeQuery()) {
        List<IncomeAttribute> list = new ArrayList<>();
        while (rs.next()) {
          list.add(IncomeAttributeDataSource.mapIncomeAttribute(rs));
        }
        return list;
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }
}
