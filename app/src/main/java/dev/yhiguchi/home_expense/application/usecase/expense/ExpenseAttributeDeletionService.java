package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseRepository;
import dev.yhiguchi.home_expense.domain.model.expense.Expenses;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class ExpenseAttributeDeletionService {

  ExpenseAttributeRepository expenseAttributeRepository;
  ExpenseRepository expenseRepository;

  public ExpenseAttributeDeletionService(
      ExpenseAttributeRepository expenseAttributeRepository, ExpenseRepository expenseRepository) {
    this.expenseAttributeRepository = expenseAttributeRepository;
    this.expenseRepository = expenseRepository;
  }

  public void delete(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn =
        identifier -> expenseAttributeRepository.get(identifier);
    Consumer<ExpenseAttributeIdentifier> deleteFn =
        identifier -> expenseAttributeRepository.delete(identifier);
    Function<ExpenseAttribute, Expenses> findExpensesFn =
        expenseAttribute -> expenseRepository.find(expenseAttribute);
    ExpenseAttributeDeleter deleter = new ExpenseAttributeDeleter(getFn, deleteFn, findExpensesFn);
    deleter.delete(expenseAttributeIdentifier);
  }
}
