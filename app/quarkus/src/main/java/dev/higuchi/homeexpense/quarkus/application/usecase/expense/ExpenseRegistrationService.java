package dev.higuchi.homeexpense.quarkus.application.usecase.expense;

import dev.higuchi.homeexpense.command.expense.ExpenseCreator;
import dev.higuchi.homeexpense.command.model.expense.*;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.quarkus.application.service.expense.ExpenseService;
import dev.higuchi.homeexpense.quarkus.application.service.expense.attribute.ExpenseAttributeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

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
