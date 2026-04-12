package dev.yhiguchi.home_expense.query.expense.attribute;

public interface ExpenseAttributeSearchResultQuerier {
  ExpenseAttributeSearchResult find(ExpenseAttributeSearchCriteria criteria);
}
