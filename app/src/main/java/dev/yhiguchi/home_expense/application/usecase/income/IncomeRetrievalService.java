package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.IncomeNotFoundException;
import dev.yhiguchi.home_expense.domain.model.income.IncomeRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IncomeRetrievalService {

  IncomeRepository incomeRepository;

  public IncomeRetrievalService(IncomeRepository incomeRepository) {
    this.incomeRepository = incomeRepository;
  }

  public Income get(IncomeIdentifier incomeIdentifier) {
    return incomeRepository.findBy(incomeIdentifier).orElseThrow(IncomeNotFoundException::new);
  }
}
