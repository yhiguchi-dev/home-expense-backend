package dev.yhiguchi.home_expense.application.service;

import dev.yhiguchi.home_expense.application.service.expense.ExpenseService;
import dev.yhiguchi.home_expense.application.service.expense.attribute.ExpenseAttributeService;
import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

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
