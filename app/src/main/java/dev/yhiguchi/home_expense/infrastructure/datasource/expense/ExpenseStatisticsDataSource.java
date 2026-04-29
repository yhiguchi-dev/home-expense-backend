package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.infrastructure.datasource.ReadOnly;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.ParameterBinder;
import dev.yhiguchi.home_expense.query.expense.*;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ExpenseStatisticsDataSource implements ExpenseStatisticsQuerier {

  private static final String SELECT_BY_CATEGORY =
      """
      SELECT
        attribute.id,
        attribute.name,
        sum(expense.expense.amount) AS total_amount
      FROM expense.expense
      INNER JOIN expense.attribute
        ON expense.attribute_id = attribute.id
      WHERE attribute.category = ?
        AND expense.payment_date >= ?
        AND expense.payment_date < ?
      GROUP BY attribute.id, attribute.name, attribute.created_at
      ORDER BY attribute.created_at
      """;

  private final JdbcOperator jdbc;

  public ExpenseStatisticsDataSource(@ReadOnly JdbcOperator jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public ExpenseStatistics search(ExpenseStatisticsCriteria criteria) {
    long incomeTotalAmount = selectIncomeTotalAmount(criteria).orElse(0L);
    List<ExpenseAttributeStatistics> fixedStatistics =
        selectByCategory(ExpenseCategory.固定費, criteria);
    List<ExpenseAttributeStatistics> variableStatistics =
        selectByCategory(ExpenseCategory.変動費, criteria);
    return new ExpenseStatistics(
        incomeTotalAmount,
        new ExpenseStatisticsDetail(fixedStatistics),
        new ExpenseStatisticsDetail(variableStatistics));
  }

  private java.util.Optional<Long> selectIncomeTotalAmount(ExpenseStatisticsCriteria criteria) {
    String sql =
        """
        SELECT sum(income.amount)
        FROM expense.income
        WHERE income.receive_date >= ? AND income.receive_date < ?
        """;
    return jdbc.queryForOptional(
        sql,
        ParameterBinder.of(criteria.dateFrom(), criteria.dateTo()),
        rs -> rs.getObject(1, Long.class));
  }

  private List<ExpenseAttributeStatistics> selectByCategory(
      ExpenseCategory category, ExpenseStatisticsCriteria criteria) {
    return jdbc.queryForList(
        SELECT_BY_CATEGORY,
        ParameterBinder.of(category.name(), criteria.dateFrom(), criteria.dateTo()),
        rs ->
            new ExpenseAttributeStatistics(
                rs.getString("id"), rs.getString("name"), rs.getLong("total_amount")));
  }
}
