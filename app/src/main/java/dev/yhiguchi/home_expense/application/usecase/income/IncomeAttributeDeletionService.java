package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.IncomeRepository;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeConstraintException;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeRepository;
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
    if (incomeRepository.existsByAttributeIdentifier(attribute.incomeAttributeIdentifier())) {
      throw new IncomeAttributeConstraintException();
    }
    incomeAttributeRepository.delete(attribute);
  }
}
