package dev.yhiguchi.home_expense.query.expense;

public interface ExpenseAggregateRepository {

  ExpenseAggregate find(ExpenseAggregateCriteria criteria);
}
