package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;

public class ExpenseAttributeAggregate {
  ExpenseAttributeIdentifier expenseAttributeIdentifier;
  ExpenseAttributeName expenseAttributeName;

  int totalAmount;

  public ExpenseAttributeAggregate(
      ExpenseAttributeIdentifier expenseAttributeIdentifier,
      ExpenseAttributeName expenseAttributeName,
      int totalAmount) {
    this.expenseAttributeIdentifier = expenseAttributeIdentifier;
    this.expenseAttributeName = expenseAttributeName;
    this.totalAmount = totalAmount;
  }

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
