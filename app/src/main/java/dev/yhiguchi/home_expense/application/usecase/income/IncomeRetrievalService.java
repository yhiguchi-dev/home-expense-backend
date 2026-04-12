package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.IncomeRepository;
import dev.yhiguchi.home_expense.query.income.IncomeSearchCriteria;
import dev.yhiguchi.home_expense.query.income.IncomeSearchResult;
import dev.yhiguchi.home_expense.query.income.IncomeSearchResultQuerier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeRetrievalService {

  IncomeRepository incomeRepository;
  IncomeSearchResultQuerier incomeSearchResultQuerier;

  public IncomeRetrievalService(
      IncomeRepository incomeRepository, IncomeSearchResultQuerier incomeSearchResultQuerier) {
    this.incomeRepository = incomeRepository;
    this.incomeSearchResultQuerier = incomeSearchResultQuerier;
  }

  public IncomeSearchResult search(IncomeSearchCriteria criteria) {
    return incomeSearchResultQuerier.find(criteria);
  }

  public Income get(IncomeIdentifier incomeIdentifier) {
    return incomeRepository.get(incomeIdentifier);
  }
}
