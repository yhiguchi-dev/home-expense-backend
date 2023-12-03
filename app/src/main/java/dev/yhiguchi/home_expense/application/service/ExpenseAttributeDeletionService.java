package dev.yhiguchi.home_expense.application.service;

import dev.yhiguchi.home_expense.application.service.expense.ExpenseService;
import dev.yhiguchi.home_expense.application.service.expense.attribute.ExpenseAttributeService;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseAttributeDeleter;
import dev.yhiguchi.home_expense.domain.model.expense.Expenses;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
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
        attribute -> expenseAttributeService.delete(expenseAttributeIdentifier);
    Function<ExpenseAttribute, Expenses> getExpensesFn =
        expenseAttribute -> expenseService.find(expenseAttribute);
    ExpenseAttributeDeleter deleter = new ExpenseAttributeDeleter(getFn, deleteFn, getExpensesFn);
    deleter.delete(expenseAttributeIdentifier);
  }
}
