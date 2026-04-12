package dev.yhiguchi.home_expense.query.expense;

public interface ExpenseSearchResultQuerier {
  ExpenseSearchResult find(ExpenseSearchCriteria criteria);
}
