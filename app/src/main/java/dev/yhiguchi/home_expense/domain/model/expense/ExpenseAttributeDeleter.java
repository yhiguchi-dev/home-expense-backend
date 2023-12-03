package dev.yhiguchi.home_expense.domain.model.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeConstraintException;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import java.util.function.Consumer;
import java.util.function.Function;

/** 経費属性の削除者 */
public class ExpenseAttributeDeleter {

  Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn;
  Consumer<ExpenseAttributeIdentifier> deleteFn;

  Function<ExpenseAttribute, Expenses> getExpensesFn;

  public ExpenseAttributeDeleter(
      Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn,
      Consumer<ExpenseAttributeIdentifier> deleteFn,
      Function<ExpenseAttribute, Expenses> getExpensesFn) {
    this.getFn = getFn;
    this.deleteFn = deleteFn;
    this.getExpensesFn = getExpensesFn;
  }

  public void delete(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    ExpenseAttribute expenseAttribute = getFn.apply(expenseAttributeIdentifier);
    Expenses expenses = getExpensesFn.apply(expenseAttribute);
    if (expenses.has(expenseAttribute)) {
      throw new ExpenseAttributeConstraintException();
    }
    deleteFn.accept(expenseAttribute.expenseAttributeIdentifier());
  }
}
