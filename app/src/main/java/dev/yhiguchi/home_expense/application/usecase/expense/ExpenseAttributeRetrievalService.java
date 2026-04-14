package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeRepository;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSearchCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSearchResult;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSearchResultQuerier;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseAttributeRetrievalService {

  ExpenseAttributeRepository expenseAttributeRepository;
  ExpenseAttributeSearchResultQuerier expenseAttributeSearchResultQuerier;

  public ExpenseAttributeRetrievalService(
      ExpenseAttributeRepository expenseAttributeRepository,
      ExpenseAttributeSearchResultQuerier expenseAttributeSearchResultQuerier) {
    this.expenseAttributeRepository = expenseAttributeRepository;
    this.expenseAttributeSearchResultQuerier = expenseAttributeSearchResultQuerier;
  }

  public ExpenseAttributeSearchResult search(ExpenseAttributeSearchCriteria criteria) {
    return expenseAttributeSearchResultQuerier.find(criteria);
  }

  public ExpenseAttribute get(ExpenseAttributeIdentifier expenseIdentifier) {
    return expenseAttributeRepository.get(expenseIdentifier);
  }
}
