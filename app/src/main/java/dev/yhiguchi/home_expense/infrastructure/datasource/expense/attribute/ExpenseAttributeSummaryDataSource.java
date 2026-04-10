package dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummary;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummaryCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummaryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

@ApplicationScoped
public class ExpenseAttributeSummaryDataSource implements ExpenseAttributeSummaryRepository {

  DataSource dataSource;

  public ExpenseAttributeSummaryDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public ExpenseAttributeSummary find(ExpenseAttributeSummaryCriteria criteria) {
    int count = selectCount(criteria);
    if (count == 0) {
      return new ExpenseAttributeSummary();
    }
    List<ExpenseAttribute> list = selectBy(criteria);
    return new ExpenseAttributeSummary(count, list);
  }

  private int selectCount(ExpenseAttributeSummaryCriteria criteria) {
    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM expense.attribute");
    List<Object> params = new ArrayList<>();
    appendCategoryFilter(criteria, sql, params);
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql.toString())) {
      setParams(ps, params);
      try (ResultSet rs = ps.executeQuery()) {
        rs.next();
        return rs.getInt(1);
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  private List<ExpenseAttribute> selectBy(ExpenseAttributeSummaryCriteria criteria) {
    StringBuilder sql = new StringBuilder("SELECT id, category, name FROM expense.attribute");
    List<Object> params = new ArrayList<>();
    appendCategoryFilter(criteria, sql, params);
    sql.append(" ORDER BY attribute.created_at OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
    params.add(criteria.offset());
    params.add(criteria.perPage());
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql.toString())) {
      setParams(ps, params);
      try (ResultSet rs = ps.executeQuery()) {
        List<ExpenseAttribute> list = new ArrayList<>();
        while (rs.next()) {
          list.add(ExpenseAttributeDataSource.mapExpenseAttribute(rs));
        }
        return list;
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  private void appendCategoryFilter(
      ExpenseAttributeSummaryCriteria criteria, StringBuilder sql, List<Object> params) {
    if (criteria.getExpenseCategory() != null) {
      sql.append(" WHERE attribute.category = ?");
      params.add(criteria.getExpenseCategory().name());
    }
  }

  private void setParams(PreparedStatement ps, List<Object> params) throws SQLException {
    for (int i = 0; i < params.size(); i++) {
      Object param = params.get(i);
      if (param instanceof Integer intVal) {
        ps.setInt(i + 1, intVal);
      } else if (param instanceof String strVal) {
        ps.setString(i + 1, strVal);
      }
    }
  }
}
