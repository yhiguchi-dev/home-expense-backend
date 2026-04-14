package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.IncomeRepository;
import dev.yhiguchi.home_expense.domain.model.income.Incomes;
import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeAttributeDeletionService {

  IncomeAttributeRepository incomeAttributeRepository;
  IncomeRepository incomeRepository;

  public IncomeAttributeDeletionService(
      IncomeAttributeRepository incomeAttributeRepository, IncomeRepository incomeRepository) {
    this.incomeAttributeRepository = incomeAttributeRepository;
    this.incomeRepository = incomeRepository;
  }

  public void delete(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    IncomeAttribute attribute = incomeAttributeRepository.get(incomeAttributeIdentifier);
    Incomes incomes = incomeRepository.find(attribute);
    if (incomes.has(attribute)) {
      throw new IncomeAttributeConstraintException();
    }
    incomeAttributeRepository.delete(incomeAttributeIdentifier);
  }
}
