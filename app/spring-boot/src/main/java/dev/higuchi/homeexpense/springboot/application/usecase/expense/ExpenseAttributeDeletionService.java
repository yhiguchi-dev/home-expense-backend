package dev.higuchi.homeexpense.springboot.application.usecase.expense;

import dev.higuchi.homeexpense.command.model.expense.Expenses;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.command.service.expense.ExpenseAttributeDeleter;
import dev.higuchi.homeexpense.springboot.application.service.expense.ExpenseService;
import dev.higuchi.homeexpense.springboot.application.service.expense.attribute.ExpenseAttributeService;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
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
