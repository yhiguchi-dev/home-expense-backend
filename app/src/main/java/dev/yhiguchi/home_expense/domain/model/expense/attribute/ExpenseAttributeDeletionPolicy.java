package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.Expenses;

/** 経費属性の削除可否を判定する */
public class ExpenseAttributeDeletionPolicy {

  @FunctionalInterface
  public interface ExpenseFinder {
    Expenses findBy(ExpenseAttribute expenseAttribute);
  }

  ExpenseFinder expenseFinder;

  public ExpenseAttributeDeletionPolicy(ExpenseFinder expenseFinder) {
    this.expenseFinder = expenseFinder;
  }

  public void assertDeletable(ExpenseAttribute expenseAttribute) {
    Expenses expenses = expenseFinder.findBy(expenseAttribute);
    if (expenses.has(expenseAttribute)) {
      throw new ExpenseAttributeConstraintException();
    }
  }
}
