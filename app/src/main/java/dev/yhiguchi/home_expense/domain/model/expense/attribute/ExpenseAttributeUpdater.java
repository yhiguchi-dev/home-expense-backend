package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import java.util.function.Consumer;
import java.util.function.Function;

/** 経費属性の更新者 */
public class ExpenseAttributeUpdater {

  Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn;
  Consumer<ExpenseAttribute> updateFn;

  public ExpenseAttributeUpdater(
      Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn,
      Consumer<ExpenseAttribute> updateFn) {
    this.getFn = getFn;
    this.updateFn = updateFn;
  }

  public void update(
      ExpenseAttributeIdentifier expenseAttributeIdentifier,
      ExpenseAttributeName expenseAttributeName,
      ExpenseCategory expenseCategory) {
    ExpenseAttribute expenseAttribute = getFn.apply(expenseAttributeIdentifier);
    ExpenseAttribute updated =
        new ExpenseAttribute(expenseAttributeIdentifier, expenseAttributeName, expenseCategory);
    if (expenseAttribute.equals(updated)) {
      return;
    }
    updateFn.accept(updated);
  }
}
