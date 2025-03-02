package dev.higuchi.homeexpense.quarkus.application.usecase.income;

import dev.higuchi.homeexpense.command.model.income.Incomes;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeIdentifier;
import dev.higuchi.homeexpense.command.service.income.IncomeAttributeDeleter;
import dev.higuchi.homeexpense.quarkus.application.service.income.IncomeService;
import dev.higuchi.homeexpense.quarkus.application.service.income.attribute.IncomeAttributeService;
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
