package dev.yhiguchi.home_expense.query.expense;

public interface ExpenseStatisticsQuerier {

  ExpenseStatistics search(ExpenseStatisticsCriteria criteria);
}
