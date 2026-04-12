package dev.yhiguchi.home_expense.query.expense;

public interface ExpenseStatisticsQuerier {

  ExpenseStatistics find(ExpenseStatisticsCriteria criteria);
}
