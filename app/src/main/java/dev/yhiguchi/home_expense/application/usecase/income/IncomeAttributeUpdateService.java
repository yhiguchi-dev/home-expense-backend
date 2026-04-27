package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.Revision;
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
    Revision<IncomeAttribute> loaded =
        incomeAttributeRepository
            .findBy(command.incomeAttributeIdentifier())
            .orElseThrow(IncomeAttributeNotFoundException::new);
    IncomeAttribute current = loaded.entity();
    incomeAttributeNameUniqueness.assertUniqueForUpdate(current, command.incomeAttributeName());
    IncomeAttribute updated = current.updateWith(command.incomeAttributeName());
    if (current.hasChanges(updated)) {
      incomeAttributeRepository.update(new Revision<>(updated, command.version()));
    }
  }
}
