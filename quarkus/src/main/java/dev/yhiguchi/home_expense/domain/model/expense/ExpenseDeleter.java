package dev.yhiguchi.home_expense.domain.model.expense;

import java.util.function.Consumer;
import java.util.function.Function;

public class ExpenseDeleter {

  Function<ExpenseIdentifier, Expense> getFn;
  Consumer<ExpenseIdentifier> deleteFn;

  public ExpenseDeleter(
      Function<ExpenseIdentifier, Expense> getFn, Consumer<ExpenseIdentifier> deleteFn) {
    this.getFn = getFn;
    this.deleteFn = deleteFn;
  }

  public void delete(ExpenseIdentifier expenseIdentifier) {
    Expense expense = getFn.apply(expenseIdentifier);
    deleteFn.accept(expense.expenseIdentifier());
  }
}
