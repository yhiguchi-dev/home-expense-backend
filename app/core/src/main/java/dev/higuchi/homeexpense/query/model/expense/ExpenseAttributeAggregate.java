package dev.higuchi.homeexpense.query.model.expense;

import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeName;

public class ExpenseAttributeAggregate {
  ExpenseAttributeIdentifier expenseAttributeIdentifier;
  ExpenseAttributeName expenseAttributeName;

  int totalAmount;

  ExpenseAttributeAggregate() {}

  public ExpenseAttributeIdentifier expenseAttributeIdentifier() {
    return expenseAttributeIdentifier;
  }

  public ExpenseAttributeName expenseAttributeName() {
    return expenseAttributeName;
  }

  public int totalAmount() {
    return totalAmount;
  }
}
