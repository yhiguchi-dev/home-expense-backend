package dev.yhiguchi.home_expense.application.usecase.income;

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
    Income income =
        incomeRepository
            .findBy(command.incomeIdentifier())
            .orElseThrow(IncomeNotFoundException::new);
    IncomeAttribute attribute =
        incomeAttributeRepository
            .findBy(command.incomeAttributeIdentifier())
            .orElseThrow(IncomeAttributeNotFoundException::new);
    Income updated =
        income.updateWith(
            command.description(),
            command.amount(),
            command.receiveDate(),
            attribute.incomeAttributeIdentifier());
    if (income.hasChanges(updated)) {
      incomeRepository.update(updated.withVersion(command.version()));
    }
  }
}
