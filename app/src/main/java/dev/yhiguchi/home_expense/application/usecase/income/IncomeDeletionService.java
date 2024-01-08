package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.application.service.income.IncomeService;
import dev.yhiguchi.home_expense.application.service.income.attribute.IncomeAttributeService;
import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.domain.model.income.IncomeDeleter;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
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
