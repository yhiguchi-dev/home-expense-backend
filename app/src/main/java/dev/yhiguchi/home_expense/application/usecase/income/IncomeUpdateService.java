package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
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

  public void update(
      IncomeIdentifier incomeIdentifier,
      Description description,
      Amount amount,
      ReceiveDate receiveDate,
      IncomeAttributeIdentifier incomeAttributeIdentifier,
      long version) {
    Income income = incomeRepository.get(incomeIdentifier);
    IncomeAttribute attribute = incomeAttributeRepository.get(incomeAttributeIdentifier);
    Income updated = income.updateWith(description, amount, receiveDate, attribute);
    if (income.hasChanges(updated)) {
      incomeRepository.update(updated.withVersion(version));
    }
  }
}
