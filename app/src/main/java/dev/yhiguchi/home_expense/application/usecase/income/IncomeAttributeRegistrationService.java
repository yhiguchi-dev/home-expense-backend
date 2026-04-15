package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeAttributeRegistrationService {

  IncomeAttributeRepository incomeAttributeRepository;
  IncomeAttributeNameUniqueness incomeAttributeNameUniqueness;

  public IncomeAttributeRegistrationService(
      IncomeAttributeRepository incomeAttributeRepository) {
    this.incomeAttributeRepository = incomeAttributeRepository;
    this.incomeAttributeNameUniqueness =
        new IncomeAttributeNameUniqueness(incomeAttributeRepository::existsByName);
  }

  public IncomeAttributeIdentifier register(IncomeAttributeName incomeAttributeName) {
    incomeAttributeNameUniqueness.assertUnique(incomeAttributeName);
    IncomeAttribute incomeAttribute = IncomeAttribute.create(incomeAttributeName);
    incomeAttributeRepository.register(incomeAttribute);
    return incomeAttribute.incomeAttributeIdentifier();
  }
}
