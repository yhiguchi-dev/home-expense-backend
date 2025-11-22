package dev.higuchi.homeexpense.springboot.application.usecase.income;

import dev.higuchi.homeexpense.command.income.IncomeAttributeDeleter;
import dev.higuchi.homeexpense.command.model.income.Incomes;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeIdentifier;
import dev.higuchi.homeexpense.springboot.application.service.income.IncomeService;
import dev.higuchi.homeexpense.springboot.application.service.income.attribute.IncomeAttributeService;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
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
    Function<IncomeAttribute, Incomes> finFn = attribute -> incomeService.find(attribute);
    IncomeAttributeDeleter deleter = new IncomeAttributeDeleter(getFn, deleteFn, finFn);
    deleter.delete(incomeAttributeIdentifier);
  }
}
