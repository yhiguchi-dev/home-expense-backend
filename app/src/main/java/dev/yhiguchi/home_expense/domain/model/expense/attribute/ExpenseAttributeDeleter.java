package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.Expenses;
import java.util.function.Consumer;
import java.util.function.Function;

/** 経費属性の削除者 */
public class ExpenseAttributeDeleter {

  Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn;
  Consumer<ExpenseAttributeIdentifier> deleteFn;

  Function<ExpenseAttribute, Expenses> findExpensesFn;

  public ExpenseAttributeDeleter(
      Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn,
      Consumer<ExpenseAttributeIdentifier> deleteFn,
      Function<ExpenseAttribute, Expenses> findExpensesFn) {
    this.getFn = getFn;
    this.deleteFn = deleteFn;
    this.findExpensesFn = findExpensesFn;
  }

  public void delete(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    ExpenseAttribute expenseAttribute = getFn.apply(expenseAttributeIdentifier);
    Expenses expenses = findExpensesFn.apply(expenseAttribute);
    if (expenses.has(expenseAttribute)) {
      throw new ExpenseAttributeConstraintException();
    }
    deleteFn.accept(expenseAttribute.expenseAttributeIdentifier());
  }
}
