package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.ParameterBinder;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResult;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResultQuerier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

@ApplicationScoped
@Transactional
public class ExpenseSearchResultDataSource implements ExpenseSearchResultQuerier {

  private final JdbcOperator jdbc;

  public ExpenseSearchResultDataSource(
      @io.quarkus.agroal.DataSource("readonly") DataSource dataSource) {
    this.jdbc = new JdbcOperator(dataSource);
  }

  @Override
  public ExpenseSearchResult find(ExpenseSearchCriteria criteria) {
    int totalCount = selectCount(criteria);
    if (totalCount == 0) {
      return new ExpenseSearchResult();
    }
    List<Expense> list = selectBy(criteria);
    return new ExpenseSearchResult(totalCount, list);
  }

  private int selectCount(ExpenseSearchCriteria criteria) {
    StringBuilder sql =
        new StringBuilder(
            """
            SELECT COUNT(*)
            FROM expense.expense
            LEFT JOIN fixed_expense
              ON expense.id = fixed_expense.expense_id
            LEFT JOIN variable_expense
              ON expense.id = variable_expense.expense_id
            LEFT JOIN attribute
              ON attribute.id = fixed_expense.attribute_id
                OR attribute.id = variable_expense.attribute_id
            """);
    List<Object> params = new ArrayList<>();
    appendWhere(criteria, sql, params);
    return jdbc.queryForOptional(sql.toString(), ParameterBinder.of(params), rs -> rs.getInt(1))
        .orElse(0);
  }

  private List<Expense> selectBy(ExpenseSearchCriteria criteria) {
    StringBuilder sql =
        new StringBuilder(
            """
            SELECT
              expense.id,
              expense.description,
              expense.price,
              expense.payment_date,
              attribute.id AS attribute_id,
              attribute.category,
              attribute.name AS attribute_name
            FROM expense.expense
            LEFT JOIN fixed_expense
              ON expense.id = fixed_expense.expense_id
            LEFT JOIN variable_expense
              ON expense.id = variable_expense.expense_id
            LEFT JOIN attribute
              ON attribute.id = fixed_expense.attribute_id
                OR attribute.id = variable_expense.attribute_id
            """);
    List<Object> params = new ArrayList<>();
    appendWhere(criteria, sql, params);
    sql.append(" ORDER BY attribute.category DESC, expense.payment_date DESC");
    sql.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
    params.add(criteria.pagination().offset());
    params.add(criteria.pagination().perPage());
    return jdbc.queryForList(
        sql.toString(), ParameterBinder.of(params), ExpenseDataSource::mapExpense);
  }

  private void appendWhere(ExpenseSearchCriteria criteria, StringBuilder sql, List<Object> params) {
    List<String> conditions = new ArrayList<>();
    if (criteria.hasDateRange()) {
      conditions.add("expense.payment_date >= ?");
      conditions.add("expense.payment_date < ?");
      params.add(Date.valueOf(criteria.dateFrom()));
      params.add(Date.valueOf(criteria.dateTo()));
    }
    if (criteria.hasExpenseCategory()) {
      conditions.add("category = ?");
      params.add(criteria.getExpenseCategory().name());
    }
    if (criteria.hasExpenseAttributeIdentifier()) {
      conditions.add("attribute.id = ?");
      params.add(criteria.getExpenseAttributeIdentifier());
    }
    if (!conditions.isEmpty()) {
      sql.append(" WHERE ");
      sql.append(String.join(" AND ", conditions));
    }
  }
}
