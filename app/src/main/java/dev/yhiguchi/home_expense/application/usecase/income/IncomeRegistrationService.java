package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.application.service.income.IncomeService;
import dev.yhiguchi.home_expense.application.service.income.attribute.IncomeAttributeService;
import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class IncomeRegistrationService {

  IncomeService incomeService;
  IncomeAttributeService incomeAttributeService;

  public IncomeRegistrationService(
      IncomeService incomeService, IncomeAttributeService incomeAttributeService) {
    this.incomeService = incomeService;
    this.incomeAttributeService = incomeAttributeService;
  }

  public IncomeIdentifier createAndRegister(
      Description description,
      Amount price,
      ReceiveDate paymentDate,
      IncomeAttributeIdentifier incomeAttributeIdentifier) {
    Function<IncomeAttributeIdentifier, IncomeAttribute> getFn =
        identifier -> incomeAttributeService.get(identifier);
    Consumer<Income> registerFn = income -> incomeService.register(income);
    IncomeCreator creator = new IncomeCreator(getFn, registerFn);
    Income income = creator.create(description, price, paymentDate, incomeAttributeIdentifier);
    return income.incomeIdentifier();
  }
}
