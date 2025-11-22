package dev.higuchi.homeexpense.command.expense;

import dev.higuchi.homeexpense.command.model.expense.Expense;
import dev.higuchi.homeexpense.command.model.expense.ExpenseIdentifier;
import java.util.function.Consumer;
import java.util.function.Function;

/** 経費の削除者 */
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
