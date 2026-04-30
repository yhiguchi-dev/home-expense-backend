package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeNameUniqueness;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeAttributeUpdateService {

  IncomeAttributeRepository incomeAttributeRepository;
  IncomeAttributeNameUniqueness incomeAttributeNameUniqueness;

  public IncomeAttributeUpdateService(IncomeAttributeRepository incomeAttributeRepository) {
    this.incomeAttributeRepository = incomeAttributeRepository;
    this.incomeAttributeNameUniqueness =
        new IncomeAttributeNameUniqueness(incomeAttributeRepository);
  }

  public void update(IncomeAttributeUpdateCommand command) {
    IncomeAttribute current = incomeAttributeRepository.get(command.incomeAttributeIdentifier());
    incomeAttributeNameUniqueness.assertUniqueForUpdate(current, command.incomeAttributeName());
    IncomeAttribute updated = current.updateWith(command.incomeAttributeName());
    if (current.hasChanges(updated)) {
      incomeAttributeRepository.update(updated, command.version());
    }
  }
}
