package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseRepository;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResult;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResultQuerier;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseRetrievalService {

  ExpenseRepository expenseRepository;
  ExpenseSearchResultQuerier expenseSearchResultQuerier;

  public ExpenseRetrievalService(
      ExpenseRepository expenseRepository, ExpenseSearchResultQuerier expenseSearchResultQuerier) {
    this.expenseRepository = expenseRepository;
    this.expenseSearchResultQuerier = expenseSearchResultQuerier;
  }

  public ExpenseSearchResult search(ExpenseSearchCriteria criteria) {
    return expenseSearchResultQuerier.find(criteria);
  }

  public Expense get(ExpenseIdentifier expenseIdentifier) {
    return expenseRepository.get(expenseIdentifier);
  }
}
