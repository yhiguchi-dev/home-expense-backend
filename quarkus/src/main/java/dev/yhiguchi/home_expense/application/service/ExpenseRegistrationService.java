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
public class ExpenseRegistrationService {

  ExpenseService expenseService;
  ExpenseAttributeService expenseAttributeService;

  public ExpenseRegistrationService(
      ExpenseService expenseService, ExpenseAttributeService expenseAttributeService) {
    this.expenseService = expenseService;
    this.expenseAttributeService = expenseAttributeService;
  }

  public ExpenseIdentifier createAndRegister(
      Description description,
      Price price,
      PaymentDate paymentDate,
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn =
        identifier -> expenseAttributeService.get(identifier);
    Consumer<Expense> registerFn = expense -> expenseService.register(expense);
    ExpenseCreator creator = new ExpenseCreator(getFn, registerFn);
    Expense expense = creator.create(description, price, paymentDate, expenseAttributeIdentifier);
    return expense.expenseIdentifier();
  }
}
