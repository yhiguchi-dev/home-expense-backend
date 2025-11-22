package dev.higuchi.homeexpense.quarkus.application.usecase.expense;

import dev.higuchi.homeexpense.command.expense.ExpenseDeleter;
import dev.higuchi.homeexpense.command.model.expense.*;
import dev.higuchi.homeexpense.quarkus.application.service.expense.ExpenseService;
import dev.higuchi.homeexpense.quarkus.application.service.expense.attribute.ExpenseAttributeService;
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
