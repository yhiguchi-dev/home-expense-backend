package dev.higuchi.homeexpense.quarkus.application.usecase.income;

import dev.higuchi.homeexpense.command.income.IncomeDeleter;
import dev.higuchi.homeexpense.command.model.income.Income;
import dev.higuchi.homeexpense.command.model.income.IncomeIdentifier;
import dev.higuchi.homeexpense.quarkus.application.service.income.IncomeService;
import dev.higuchi.homeexpense.quarkus.application.service.income.attribute.IncomeAttributeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class IncomeDeletionService {

  IncomeService incomeService;
  IncomeAttributeService incomeAttributeService;

  public IncomeDeletionService(
      IncomeService incomeService, IncomeAttributeService incomeAttributeService) {
    this.incomeService = incomeService;
    this.incomeAttributeService = incomeAttributeService;
  }

  public void delete(IncomeIdentifier incomeIdentifier) {
    Function<IncomeIdentifier, Income> getFn = identifier -> incomeService.get(identifier);
    Consumer<IncomeIdentifier> deleteFn = identifier -> incomeService.delete(identifier);
    IncomeDeleter deleter = new IncomeDeleter(getFn, deleteFn);
    deleter.delete(incomeIdentifier);
  }
}
