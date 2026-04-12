package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;

public class ExpenseAttributeStatistics {
  ExpenseAttributeIdentifier expenseAttributeIdentifier;
  ExpenseAttributeName expenseAttributeName;

  long totalAmount;

  public ExpenseAttributeStatistics(
      ExpenseAttributeIdentifier expenseAttributeIdentifier,
      ExpenseAttributeName expenseAttributeName,
      long totalAmount) {
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

  public long totalAmount() {
    return totalAmount;
  }
}
