package dev.higuchi.homeexpense.command.expense;

import dev.higuchi.homeexpense.command.model.expense.ExpenseCategory;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeName;
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
