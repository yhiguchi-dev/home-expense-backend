package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

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
      Amount price,
      ReceiveDate paymentDate,
      IncomeAttributeIdentifier incomeAttributeIdentifier) {
    Function<IncomeAttributeIdentifier, IncomeAttribute> getFn =
        identifier -> incomeAttributeRepository.get(identifier);
    Consumer<Income> registerFn = income -> incomeRepository.register(income);
    IncomeCreator creator = new IncomeCreator(getFn, registerFn);
    Income income = creator.create(description, price, paymentDate, incomeAttributeIdentifier);
    return income.incomeIdentifier();
  }
}
