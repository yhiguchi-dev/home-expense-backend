package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

/** 経費属性作成者 */
public class ExpenseAttributeCreator {

  Function<ExpenseAttributeName, ExpenseAttribute> findFn;
  Consumer<ExpenseAttribute> registerFn;

  public ExpenseAttributeCreator(
      Function<ExpenseAttributeName, ExpenseAttribute> findFn,
      Consumer<ExpenseAttribute> registerFn) {
    this.findFn = findFn;
    this.registerFn = registerFn;
  }

  public ExpenseAttribute createAndRegister(
      ExpenseAttributeName expenseAttributeName, ExpenseCategory expenseCategory) {
    throwIfExpenseAttributeAlreadyExists(expenseAttributeName);
    ExpenseAttributeIdentifier expenseAttributeIdentifier =
        new ExpenseAttributeIdentifier(UUID.randomUUID().toString());
    ExpenseAttribute expenseAttribute =
        new ExpenseAttribute(expenseAttributeIdentifier, expenseAttributeName, expenseCategory);
    registerFn.accept(expenseAttribute);
    return expenseAttribute;
  }

  void throwIfExpenseAttributeAlreadyExists(ExpenseAttributeName expenseAttributeName) {
    ExpenseAttribute expenseAttribute = findFn.apply(expenseAttributeName);
    if (expenseAttribute.exists()) {
      throw new ExpenseAttributeAlreadyExistsException();
    }
  }
}
