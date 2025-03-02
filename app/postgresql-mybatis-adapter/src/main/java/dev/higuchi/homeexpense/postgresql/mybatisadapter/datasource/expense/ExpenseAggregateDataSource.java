package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense;

import dev.higuchi.homeexpense.query.model.expense.*;
import java.util.List;

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
