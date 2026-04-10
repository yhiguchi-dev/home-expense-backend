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
public class ExpenseUpdateService {

  ExpenseRepository expenseRepository;
  ExpenseAttributeRepository expenseAttributeRepository;

  public ExpenseUpdateService(
      ExpenseRepository expenseRepository, ExpenseAttributeRepository expenseAttributeRepository) {
    this.expenseRepository = expenseRepository;
    this.expenseAttributeRepository = expenseAttributeRepository;
  }

  public void update(
      ExpenseIdentifier expenseIdentifier,
      Description description,
      Price price,
      PaymentDate paymentDate,
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    Function<ExpenseIdentifier, Expense> getFn = identifier -> expenseRepository.get(identifier);
    Function<ExpenseAttributeIdentifier, ExpenseAttribute> getAttributeFn =
        identifier -> expenseAttributeRepository.get(identifier);
    Consumer<Expense> updateFn = expense -> expenseRepository.update(expense);
    ExpenseUpdater updater = new ExpenseUpdater(getFn, getAttributeFn, updateFn);
    updater.update(expenseIdentifier, description, price, paymentDate, expenseAttributeIdentifier);
  }
}
