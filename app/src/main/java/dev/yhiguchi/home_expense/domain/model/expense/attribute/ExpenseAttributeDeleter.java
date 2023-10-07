package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import java.util.function.Consumer;
import java.util.function.Function;

public class ExpenseAttributeDeleter {

  Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn;
  Consumer<ExpenseAttributeIdentifier> deleteFn;

  public ExpenseAttributeDeleter(
      Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn,
      Consumer<ExpenseAttributeIdentifier> deleteFn) {
    this.getFn = getFn;
    this.deleteFn = deleteFn;
  }

  public void delete(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    ExpenseAttribute expenseAttribute = getFn.apply(expenseAttributeIdentifier);
    deleteFn.accept(expenseAttribute.expenseAttributeIdentifier());
  }
}
