package dev.yhiguchi.home_expense.query.income;

public interface IncomeSearchResultQuerier {
  IncomeSearchResult find(IncomeSearchCriteria criteria);
}
