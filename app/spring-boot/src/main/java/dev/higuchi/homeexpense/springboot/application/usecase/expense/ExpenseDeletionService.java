package dev.higuchi.homeexpense.springboot.application.usecase.expense;

import dev.higuchi.homeexpense.command.model.expense.Expense;
import dev.higuchi.homeexpense.command.model.expense.ExpenseIdentifier;
import dev.higuchi.homeexpense.command.service.expense.ExpenseDeleter;
import dev.higuchi.homeexpense.springboot.application.service.expense.ExpenseService;
import dev.higuchi.homeexpense.springboot.application.service.expense.attribute.ExpenseAttributeService;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
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
