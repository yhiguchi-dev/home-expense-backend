package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.application.service.expense.ExpenseService;
import dev.yhiguchi.home_expense.application.service.expense.attribute.ExpenseAttributeService;
import dev.yhiguchi.home_expense.domain.model.expense.Expenses;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeDeleter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class ExpenseAttributeDeletionService {

  ExpenseAttributeService expenseAttributeService;
  ExpenseService expenseService;

  public ExpenseAttributeDeletionService(
      ExpenseAttributeService expenseAttributeService, ExpenseService expenseService) {
    this.expenseAttributeService = expenseAttributeService;
    this.expenseService = expenseService;
  }

  public void delete(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn =
        identifier -> expenseAttributeService.get(identifier);
    Consumer<ExpenseAttributeIdentifier> deleteFn =
        identifier -> expenseAttributeService.delete(identifier);
    Function<ExpenseAttribute, Expenses> findExpensesFn =
        expenseAttribute -> expenseService.find(expenseAttribute);
    ExpenseAttributeDeleter deleter = new ExpenseAttributeDeleter(getFn, deleteFn, findExpensesFn);
    deleter.delete(expenseAttributeIdentifier);
  }
}
