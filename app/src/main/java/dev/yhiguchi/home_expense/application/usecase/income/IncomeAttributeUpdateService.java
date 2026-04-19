package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeNameUniqueness;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeNotFoundException;
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
        new IncomeAttributeNameUniqueness(incomeAttributeRepository::existsByName);
  }

  public void update(IncomeAttributeUpdateCommand command) {
    IncomeAttribute attribute =
        incomeAttributeRepository
            .findBy(command.incomeAttributeIdentifier())
            .orElseThrow(IncomeAttributeNotFoundException::new);
    if (!attribute.hasSameName(command.incomeAttributeName())) {
      incomeAttributeNameUniqueness.assertUnique(command.incomeAttributeName());
    }
    IncomeAttribute updated = attribute.updateWith(command.incomeAttributeName());
    if (attribute.hasChanges(updated)) {
      incomeAttributeRepository.update(updated.withVersion(command.version()));
    }
  }
}
