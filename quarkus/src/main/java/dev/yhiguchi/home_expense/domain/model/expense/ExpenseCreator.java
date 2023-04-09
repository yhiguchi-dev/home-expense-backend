package dev.yhiguchi.home_expense.domain.model.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

/** 経費作成者 */
public class ExpenseCreator {

  Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn;
  Consumer<Expense> registerFn;

  public ExpenseCreator(
      Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn, Consumer<Expense> registerFn) {
    this.getFn = getFn;
    this.registerFn = registerFn;
  }

  public Expense create(
      Description description,
      Price price,
      PaymentDate paymentDate,
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    ExpenseIdentifier expenseIdentifier = new ExpenseIdentifier(UUID.randomUUID().toString());
    if (expenseAttributeIdentifier.exists()) {
      ExpenseAttribute expenseAttribute = getFn.apply(expenseAttributeIdentifier);
      Expense expense =
          new Expense(expenseIdentifier, description, price, paymentDate, expenseAttribute);
      registerFn.accept(expense);
      return expense;
    }
    Expense expense =
        new Expense(expenseIdentifier, description, price, paymentDate, new ExpenseAttribute());
    registerFn.accept(expense);
    return expense;
  }
}
