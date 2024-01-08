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
public class IncomeUpdateService {

  IncomeService incomeService;
  IncomeAttributeService incomeAttributeService;

  public IncomeUpdateService(
      IncomeService incomeService, IncomeAttributeService incomeAttributeService) {
    this.incomeService = incomeService;
    this.incomeAttributeService = incomeAttributeService;
  }

  public void update(
      IncomeIdentifier incomeIdentifier,
      Description description,
      Amount price,
      ReceiveDate receiveDate,
      IncomeAttributeIdentifier incomeAttributeIdentifier) {
    Function<IncomeIdentifier, Income> getFn = identifier -> incomeService.get(identifier);
    Function<IncomeAttributeIdentifier, IncomeAttribute> getAttributeFn =
        identifier -> incomeAttributeService.get(identifier);
    Consumer<Income> updateFn = income -> incomeService.update(income);
    IncomeUpdater updater = new IncomeUpdater(getFn, getAttributeFn, updateFn);
    updater.update(incomeIdentifier, description, price, receiveDate, incomeAttributeIdentifier);
  }
}
