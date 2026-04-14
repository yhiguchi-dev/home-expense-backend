package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.IncomeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeDeletionService {

  IncomeRepository incomeRepository;

  public IncomeDeletionService(IncomeRepository incomeRepository) {
    this.incomeRepository = incomeRepository;
  }

  public void delete(IncomeIdentifier incomeIdentifier) {
    incomeRepository.get(incomeIdentifier);
    incomeRepository.delete(incomeIdentifier);
  }
}
