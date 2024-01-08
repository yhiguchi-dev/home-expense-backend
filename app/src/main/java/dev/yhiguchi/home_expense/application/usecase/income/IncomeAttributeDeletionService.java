package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.application.service.income.IncomeService;
import dev.yhiguchi.home_expense.application.service.income.attribute.IncomeAttributeService;
import dev.yhiguchi.home_expense.domain.model.income.Incomes;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeDeleter;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class IncomeAttributeDeletionService {

  IncomeAttributeService incomeAttributeService;
  IncomeService incomeService;

  public IncomeAttributeDeletionService(
      IncomeAttributeService incomeAttributeService, IncomeService incomeService) {
    this.incomeAttributeService = incomeAttributeService;
    this.incomeService = incomeService;
  }

  public void delete(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    Function<IncomeAttributeIdentifier, IncomeAttribute> getFn =
        identifier -> incomeAttributeService.get(identifier);
    Consumer<IncomeAttributeIdentifier> deleteFn =
        identifier -> incomeAttributeService.delete(identifier);
    Function<IncomeAttribute, Incomes> findExpensesFn = attribute -> incomeService.find(attribute);
    IncomeAttributeDeleter deleter = new IncomeAttributeDeleter(getFn, deleteFn, findExpensesFn);
    deleter.delete(incomeAttributeIdentifier);
  }
}
