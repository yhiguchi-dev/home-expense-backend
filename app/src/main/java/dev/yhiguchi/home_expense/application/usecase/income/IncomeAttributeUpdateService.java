package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeAttributeUpdateService {

  IncomeAttributeRepository incomeAttributeRepository;

  public IncomeAttributeUpdateService(IncomeAttributeRepository incomeAttributeRepository) {
    this.incomeAttributeRepository = incomeAttributeRepository;
  }

  public void update(
      IncomeAttributeIdentifier incomeAttributeIdentifier,
      IncomeAttributeName incomeAttributeName,
      long version) {
    IncomeAttribute attribute = incomeAttributeRepository.get(incomeAttributeIdentifier);
    IncomeAttribute updated = attribute.updateWith(incomeAttributeName);
    if (attribute.hasChanges(updated)) {
      incomeAttributeRepository.update(updated.withVersion(version));
    }
  }
}
