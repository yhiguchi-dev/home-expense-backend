package dev.higuchi.homeexpense.springboot.application.usecase.income;

import dev.higuchi.homeexpense.command.model.income.*;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeIdentifier;
import dev.higuchi.homeexpense.command.service.income.IncomeCreator;
import dev.higuchi.homeexpense.springboot.application.service.income.IncomeService;
import dev.higuchi.homeexpense.springboot.application.service.income.attribute.IncomeAttributeService;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
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
