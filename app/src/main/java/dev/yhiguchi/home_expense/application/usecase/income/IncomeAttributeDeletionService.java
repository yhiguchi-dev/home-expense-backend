package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.IncomeRepository;
import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeAttributeDeletionService {

  IncomeAttributeRepository incomeAttributeRepository;
  IncomeAttributeDeletionPolicy incomeAttributeDeletionPolicy;

  public IncomeAttributeDeletionService(
      IncomeAttributeRepository incomeAttributeRepository,
      IncomeRepository incomeRepository) {
    this.incomeAttributeRepository = incomeAttributeRepository;
    this.incomeAttributeDeletionPolicy =
        new IncomeAttributeDeletionPolicy(incomeRepository::find);
  }

  public void delete(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    IncomeAttribute attribute = incomeAttributeRepository.get(incomeAttributeIdentifier);
    incomeAttributeDeletionPolicy.assertDeletable(attribute);
    incomeAttributeRepository.delete(attribute);
  }
}
