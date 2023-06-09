package dev.yhiguchi.home_expense.application.service;

import dev.yhiguchi.home_expense.application.service.expense.ExpenseService;
import dev.yhiguchi.home_expense.application.service.expense.attribute.ExpenseAttributeService;
import dev.yhiguchi.home_expense.domain.model.expense.*;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseDeletionService {

  ExpenseService expenseService;
  ExpenseAttributeService expenseAttributeService;

  public ExpenseDeletionService(
      ExpenseService expenseService, ExpenseAttributeService expenseAttributeService) {
    this.expenseService = expenseService;
    this.expenseAttributeService = expenseAttributeService;
  }

  public void delete(ExpenseIdentifier expenseIdentifier) {
    Function<ExpenseIdentifier, Expense> getFn = identifier -> expenseService.get(identifier);
    Consumer<ExpenseIdentifier> deleteFn = expense -> expenseService.delete(expenseIdentifier);
    ExpenseDeleter deleter = new ExpenseDeleter(getFn, deleteFn);
    deleter.delete(expenseIdentifier);
  }
}
