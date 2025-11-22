package dev.higuchi.homeexpense.quarkus.application.usecase.income;

import dev.higuchi.homeexpense.command.income.IncomeCreator;
import dev.higuchi.homeexpense.command.model.income.*;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeIdentifier;
import dev.higuchi.homeexpense.quarkus.application.service.income.IncomeService;
import dev.higuchi.homeexpense.quarkus.application.service.income.attribute.IncomeAttributeService;
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
