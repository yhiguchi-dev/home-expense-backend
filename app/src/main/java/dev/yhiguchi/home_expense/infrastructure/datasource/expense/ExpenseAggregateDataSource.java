package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
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
    int fixedTotalAmount =
        expenseAggregateMapper.selectTotalAmountByFixedCategory(criteria).orElse(0);
    int variableTotalAmount =
        expenseAggregateMapper.selectTotalAmountByVariableCategory(criteria).orElse(0);
    List<Expense> fixedExpenses = expenseAggregateMapper.selectByFixedCategory(criteria);
    List<Expense> variableExpenses = expenseAggregateMapper.selectByVariableCategory(criteria);
    return new ExpenseAggregate(
        new ExpenseAggregateDetail(fixedTotalAmount, fixedExpenses),
        new ExpenseAggregateDetail(variableTotalAmount, variableExpenses));
  }
}
