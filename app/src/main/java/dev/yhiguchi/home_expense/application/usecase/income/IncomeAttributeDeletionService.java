package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.IncomeRepository;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeDeletionPolicy;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeNotFoundException;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeAttributeDeletionService {

  IncomeAttributeRepository incomeAttributeRepository;
  IncomeAttributeDeletionPolicy incomeAttributeDeletionPolicy;

  public IncomeAttributeDeletionService(
      IncomeAttributeRepository incomeAttributeRepository, IncomeRepository incomeRepository) {
    this.incomeAttributeRepository = incomeAttributeRepository;
    this.incomeAttributeDeletionPolicy =
        new IncomeAttributeDeletionPolicy(incomeRepository::existsByAttributeIdentifier);
  }

  public void delete(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    IncomeAttribute attribute =
        incomeAttributeRepository
            .findBy(incomeAttributeIdentifier)
            .orElseThrow(IncomeAttributeNotFoundException::new);
    incomeAttributeDeletionPolicy.assertDeletable(attribute);
    incomeAttributeRepository.delete(attribute);
  }
}
