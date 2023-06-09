package dev.yhiguchi.home_expense.domain.model.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

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
    if (expenseAttributeIdentifier.exists()) {
      ExpenseAttribute expenseAttribute = getAttributeFn.apply(expenseAttributeIdentifier);
      Expense updated =
          new Expense(expenseIdentifier, description, price, paymentDate, expenseAttribute);
      if (Objects.equals(expense, updated)) {
        return;
      }
      updateFn.accept(updated);
      return;
    }

    Expense updated =
        new Expense(expenseIdentifier, description, price, paymentDate, new ExpenseAttribute());
    if (Objects.equals(expense, updated)) {
      return;
    }
    updateFn.accept(updated);
  }
}
