package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class ExpenseDeletionService {

  ExpenseRepository expenseRepository;

  public ExpenseDeletionService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  public void delete(ExpenseIdentifier expenseIdentifier) {
    Function<ExpenseIdentifier, Expense> getFn = identifier -> expenseRepository.get(identifier);
    Consumer<ExpenseIdentifier> deleteFn = identifier -> expenseRepository.delete(identifier);
    ExpenseDeleter deleter = new ExpenseDeleter(getFn, deleteFn);
    deleter.delete(expenseIdentifier);
  }
}
