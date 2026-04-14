package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
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

  public IncomeIdentifier createAndRegister(
      Description description,
      Amount amount,
      ReceiveDate receiveDate,
      IncomeAttributeIdentifier incomeAttributeIdentifier) {
    IncomeAttribute attribute = incomeAttributeRepository.get(incomeAttributeIdentifier);
    Income income = Income.create(description, amount, receiveDate, attribute);
    incomeRepository.register(income);
    return income.incomeIdentifier();
  }
}
