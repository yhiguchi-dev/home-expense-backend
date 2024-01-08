package dev.yhiguchi.home_expense.domain.model.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;

/** 収入 */
public class Income {
  IncomeIdentifier incomeIdentifier;
  Description description;
  Amount amount;
  ReceiveDate receiveDate;

  IncomeAttribute incomeAttribute;

  public Income(
      IncomeIdentifier incomeIdentifier,
      Description description,
      Amount amount,
      ReceiveDate receiveDate,
      IncomeAttribute incomeAttribute) {
    this.incomeIdentifier = incomeIdentifier;
    this.description = description;
    this.amount = amount;
    this.receiveDate = receiveDate;
    this.incomeAttribute = incomeAttribute;
  }

  Income() {}

  public IncomeIdentifier incomeIdentifier() {
    return incomeIdentifier;
  }

  public Description description() {
    return description;
  }

  public Amount amount() {
    return amount;
  }

  public ReceiveDate receiveDate() {
    return receiveDate;
  }

  public IncomeAttribute incomeAttribute() {
    return incomeAttribute;
  }
}
