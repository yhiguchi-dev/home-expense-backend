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
public class IncomeRegistrationService {

  IncomeRepository incomeRepository;
  IncomeAttributeRepository incomeAttributeRepository;

  public IncomeRegistrationService(
      IncomeRepository incomeRepository, IncomeAttributeRepository incomeAttributeRepository) {
    this.incomeRepository = incomeRepository;
    this.incomeAttributeRepository = incomeAttributeRepository;
  }

  public IncomeIdentifier register(IncomeRegistrationCommand command) {
    IncomeAttribute attribute =
        incomeAttributeRepository
            .findBy(command.incomeAttributeIdentifier())
            .map(Revision::entity)
            .orElseThrow(IncomeAttributeNotFoundException::new);
    Income income =
        Income.create(
            command.description(),
            command.amount(),
            command.receiveDate(),
            attribute.incomeAttributeIdentifier());
    incomeRepository.register(income);
    return income.incomeIdentifier();
  }
}
