package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeNotFoundException;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeUpdateService {

  IncomeRepository incomeRepository;
  IncomeAttributeRepository incomeAttributeRepository;

  public IncomeUpdateService(
      IncomeRepository incomeRepository, IncomeAttributeRepository incomeAttributeRepository) {
    this.incomeRepository = incomeRepository;
    this.incomeAttributeRepository = incomeAttributeRepository;
  }

  public void update(IncomeUpdateCommand command) {
    Revision<Income> loaded =
        incomeRepository
            .findBy(command.incomeIdentifier())
            .orElseThrow(IncomeNotFoundException::new);
    IncomeAttribute attribute =
        incomeAttributeRepository
            .findBy(command.incomeAttributeIdentifier())
            .map(Revision::entity)
            .orElseThrow(IncomeAttributeNotFoundException::new);
    Income current = loaded.entity();
    Income updated =
        current.updateWith(
            command.description(),
            command.amount(),
            command.receiveDate(),
            attribute.incomeAttributeIdentifier());
    if (current.hasChanges(updated)) {
      incomeRepository.update(new Revision<>(updated, command.version()));
    }
  }
}
