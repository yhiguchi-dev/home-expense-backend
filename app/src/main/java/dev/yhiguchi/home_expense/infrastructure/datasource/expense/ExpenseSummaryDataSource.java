package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummary;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummaryCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummaryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

@ApplicationScoped
public class ExpenseSummaryDataSource implements ExpenseSummaryRepository {

  DataSource dataSource;

  public ExpenseSummaryDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public ExpenseSummary find(ExpenseSummaryCriteria criteria) {
    StringBuilder sql = new StringBuilder();
    sql.append(
        """
        SELECT
          expense.id,
          expense.description,
          expense.price,
          expense.payment_date,
          attribute.id AS attribute_id,
          attribute.category,
          attribute.name AS attribute_name,
          COUNT(*) OVER() AS total_count
        FROM expense.expense
        LEFT JOIN fixed_expense
          ON expense.id = fixed_expense.expense_id
        LEFT JOIN variable_expense
          ON expense.id = variable_expense.expense_id
        LEFT JOIN attribute
          ON attribute.id = fixed_expense.attribute_id
            OR attribute.id = variable_expense.attribute_id
        WHERE 1=1
        """);
    List<Object> params = buildWhereParams(criteria, sql);
    sql.append(" ORDER BY attribute.category DESC, expense.payment_date DESC");
    sql.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
    params.add(criteria.pagination().offset());
    params.add(criteria.pagination().perPage());
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql.toString())) {
      setParams(ps, params);
      try (ResultSet rs = ps.executeQuery()) {
        List<Expense> list = new ArrayList<>();
        int totalCount = 0;
        while (rs.next()) {
          list.add(ExpenseDataSource.mapExpense(rs));
          totalCount = rs.getInt("total_count");
        }
        if (list.isEmpty()) {
          return new ExpenseSummary();
        }
        return new ExpenseSummary(totalCount, list);
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  private List<Object> buildWhereParams(ExpenseSummaryCriteria criteria, StringBuilder sql) {
    List<Object> params = new ArrayList<>();
    if (criteria.hasDateRange()) {
      sql.append(" AND expense.payment_date >= ? AND expense.payment_date < ?");
      params.add(Date.valueOf(criteria.dateFrom()));
      params.add(Date.valueOf(criteria.dateTo()));
    }
    if (criteria.hasExpenseCategory()) {
      sql.append(" AND category = ?");
      params.add(criteria.getExpenseCategory().name());
    }
    if (criteria.hasExpenseAttributeIdentifier()) {
      sql.append(" AND attribute.id = ?");
      params.add(criteria.getExpenseAttributeIdentifier());
    }
    return params;
  }

  private void setParams(PreparedStatement ps, List<Object> params) throws SQLException {
    for (int i = 0; i < params.size(); i++) {
      Object param = params.get(i);
      if (param instanceof Integer intVal) {
        ps.setInt(i + 1, intVal);
      } else if (param instanceof String strVal) {
        ps.setString(i + 1, strVal);
      } else if (param instanceof Date dateVal) {
        ps.setDate(i + 1, dateVal);
      }
    }
  }
}
