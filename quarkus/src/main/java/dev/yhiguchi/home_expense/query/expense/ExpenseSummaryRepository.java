package dev.yhiguchi.home_expense.query.expense;

public interface ExpenseSummaryRepository {
  ExpenseSummary find(ExpenseCriteria expenseCriteria);
}
