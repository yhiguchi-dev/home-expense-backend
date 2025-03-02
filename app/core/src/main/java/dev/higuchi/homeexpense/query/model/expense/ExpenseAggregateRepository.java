package dev.higuchi.homeexpense.query.model.expense;

public interface ExpenseAggregateRepository {

  ExpenseAggregate find(ExpenseAggregateCriteria criteria);
}
