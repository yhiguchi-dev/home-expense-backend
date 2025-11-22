package dev.higuchi.homeexpense.command.expense;

import dev.higuchi.homeexpense.command.model.expense.*;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import java.util.function.Consumer;
import java.util.function.Function;

/** 経費の更新者 */
public class ExpenseUpdater {

  Function<ExpenseIdentifier, Expense> getFn;

  Function<ExpenseAttributeIdentifier, ExpenseAttribute> getAttributeFn;
  Consumer<Expense> updateFn;

  public ExpenseUpdater(
      Function<ExpenseIdentifier, Expense> getFn,
      Function<ExpenseAttributeIdentifier, ExpenseAttribute> getAttributeFn,
      Consumer<Expense> updateFn) {
    this.getFn = getFn;
    this.getAttributeFn = getAttributeFn;
    this.updateFn = updateFn;
  }

  public void update(
      ExpenseIdentifier expenseIdentifier,
      Description description,
      Price price,
      PaymentDate paymentDate,
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    Expense expense = getFn.apply(expenseIdentifier);
    ExpenseAttribute expenseAttribute = getAttributeFn.apply(expenseAttributeIdentifier);
    Expense updated =
        new Expense(expenseIdentifier, description, price, paymentDate, expenseAttribute);
    if (expense.equals(updated)) {
      return;
    }
    updateFn.accept(updated);
  }
}
