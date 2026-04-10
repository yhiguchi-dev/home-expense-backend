package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class ExpenseRegistrationService {

  ExpenseRepository expenseRepository;
  ExpenseAttributeRepository expenseAttributeRepository;

  public ExpenseRegistrationService(
      ExpenseRepository expenseRepository, ExpenseAttributeRepository expenseAttributeRepository) {
    this.expenseRepository = expenseRepository;
    this.expenseAttributeRepository = expenseAttributeRepository;
  }

  public ExpenseIdentifier createAndRegister(
      Description description,
      Price price,
      PaymentDate paymentDate,
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn =
        identifier -> expenseAttributeRepository.get(identifier);
    Consumer<Expense> registerFn = expense -> expenseRepository.register(expense);
    ExpenseCreator creator = new ExpenseCreator(getFn, registerFn);
    Expense expense = creator.create(description, price, paymentDate, expenseAttributeIdentifier);
    return expense.expenseIdentifier();
  }
}
