package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import dev.yhiguchi.home_expense.query.expense.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.sql.Date;
import java.util.List;
import javax.sql.DataSource;

@ApplicationScoped
@Transactional
public class ExpenseStatisticsDataSource implements ExpenseStatisticsQuerier {

  private final JdbcOperator jdbc;

  public ExpenseStatisticsDataSource(
      @io.quarkus.agroal.DataSource("readonly") DataSource dataSource) {
    this.jdbc = new JdbcOperator(dataSource);
  }

  @Override
  public ExpenseStatistics find(ExpenseStatisticsCriteria criteria) {
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
        ps -> {
          ps.setDate(1, Date.valueOf(criteria.dateFrom()));
          ps.setDate(2, Date.valueOf(criteria.dateTo()));
        },
        rs -> rs.getObject(1, Long.class));
  }

  private List<ExpenseAttributeStatistics> selectByCategory(
      ExpenseCategory category, ExpenseStatisticsCriteria criteria) {
    return jdbc.queryForList(
        buildCategorySql(category),
        ps -> {
          ps.setDate(1, Date.valueOf(criteria.dateFrom()));
          ps.setDate(2, Date.valueOf(criteria.dateTo()));
        },
        rs ->
            new ExpenseAttributeStatistics(
                new ExpenseAttributeIdentifier(rs.getString("id")),
                new ExpenseAttributeName(rs.getString("name")),
                rs.getLong("total_amount")));
  }

  private static String buildCategorySql(ExpenseCategory category) {
    String junctionTable =
        switch (category) {
          case 固定費 -> "fixed_expense";
          case 変動費 -> "variable_expense";
        };
    return """
        SELECT
          attribute.id,
          attribute.name,
          sum(expense.expense.price) AS total_amount
        FROM expense.expense
        LEFT JOIN %s
          ON expense.id = %s.expense_id
        LEFT JOIN attribute
          ON attribute.id = %s.attribute_id
        WHERE attribute.id IS NOT NULL
        AND expense.payment_date >= ? AND expense.payment_date < ?
        GROUP BY attribute.id, attribute.name, attribute.created_at
        ORDER BY attribute.created_at
        """
        .formatted(junctionTable, junctionTable, junctionTable);
  }
}
