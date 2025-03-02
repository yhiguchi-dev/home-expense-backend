package dev.higuchi.homeexpense.quarkus.application.usecase.expense;

import dev.higuchi.homeexpense.command.model.expense.*;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.command.service.expense.ExpenseUpdater;
import dev.higuchi.homeexpense.quarkus.application.service.expense.ExpenseService;
import dev.higuchi.homeexpense.quarkus.application.service.expense.attribute.ExpenseAttributeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class ExpenseUpdateService {

  ExpenseService expenseService;
  ExpenseAttributeService expenseAttributeService;

  public ExpenseUpdateService(
      ExpenseService expenseService, ExpenseAttributeService expenseAttributeService) {
    this.expenseService = expenseService;
    this.expenseAttributeService = expenseAttributeService;
  }

  public void update(
      ExpenseIdentifier expenseIdentifier,
      Description description,
      Price price,
      PaymentDate paymentDate,
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    Function<ExpenseIdentifier, Expense> getFn = identifier -> expenseService.get(identifier);
    Function<ExpenseAttributeIdentifier, ExpenseAttribute> getAttributeFn =
        identifier -> expenseAttributeService.get(identifier);
    Consumer<Expense> updateFn = expense -> expenseService.update(expense);
    ExpenseUpdater updater = new ExpenseUpdater(getFn, getAttributeFn, updateFn);
    updater.update(expenseIdentifier, description, price, paymentDate, expenseAttributeIdentifier);
  }
}
