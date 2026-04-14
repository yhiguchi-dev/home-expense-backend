package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeRepository;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchCriteria;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchResult;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchResultQuerier;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IncomeAttributeRetrievalService {

  IncomeAttributeRepository incomeAttributeRepository;
  IncomeAttributeSearchResultQuerier incomeAttributeSearchResultQuerier;

  public IncomeAttributeRetrievalService(
      IncomeAttributeRepository incomeAttributeRepository,
      IncomeAttributeSearchResultQuerier incomeAttributeSearchResultQuerier) {
    this.incomeAttributeRepository = incomeAttributeRepository;
    this.incomeAttributeSearchResultQuerier = incomeAttributeSearchResultQuerier;
  }

  public IncomeAttributeSearchResult search(IncomeAttributeSearchCriteria criteria) {
    return incomeAttributeSearchResultQuerier.find(criteria);
  }

  public IncomeAttribute get(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return incomeAttributeRepository.get(incomeAttributeIdentifier);
  }
}
