package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.application.service.expense.ExpenseService;
import dev.yhiguchi.home_expense.application.service.expense.attribute.ExpenseAttributeService;
import dev.yhiguchi.home_expense.domain.model.expense.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

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
    Consumer<ExpenseIdentifier> deleteFn = identifier -> expenseService.delete(identifier);
    ExpenseDeleter deleter = new ExpenseDeleter(getFn, deleteFn);
    deleter.delete(expenseIdentifier);
  }
}
