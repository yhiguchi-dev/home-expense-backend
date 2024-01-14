package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.query.expense.*;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ExpenseAggregateDataSource implements ExpenseAggregateRepository {
  ExpenseAggregateMapper expenseAggregateMapper;

  public ExpenseAggregateDataSource(ExpenseAggregateMapper expenseAggregateMapper) {
    this.expenseAggregateMapper = expenseAggregateMapper;
  }

  @Override
  public ExpenseAggregate find(ExpenseAggregateCriteria criteria) {
    int incomeTotalAmount = expenseAggregateMapper.selectIncomeTotalAmount(criteria).orElse(0);
    int fixedTotalAmount =
        expenseAggregateMapper.selectTotalAmountByFixedCategory(criteria).orElse(0);
    int variableTotalAmount =
        expenseAggregateMapper.selectTotalAmountByVariableCategory(criteria).orElse(0);
    List<ExpenseAttributeAggregate> fixedAttributeAggregate =
        expenseAggregateMapper.selectByFixedCategory(criteria);
    List<ExpenseAttributeAggregate> variableAttributeAggregate =
        expenseAggregateMapper.selectByVariableCategory(criteria);
    return new ExpenseAggregate(
        incomeTotalAmount,
        new ExpenseAggregateDetail(fixedTotalAmount, fixedAttributeAggregate),
        new ExpenseAggregateDetail(variableTotalAmount, variableAttributeAggregate));
  }
}
