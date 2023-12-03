package dev.yhiguchi.home_expense.domain.model.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;

/** 収入 */
public class Income {
  IncomeIdentifier incomeIdentifier;
  Amount amount;

  IncomeAttribute incomeAttribute;

  public Income(IncomeIdentifier incomeIdentifier, Amount amount, IncomeAttribute incomeAttribute) {
    this.incomeIdentifier = incomeIdentifier;
    this.amount = amount;
    this.incomeAttribute = incomeAttribute;
  }

  public IncomeIdentifier incomeIdentifier() {
    return incomeIdentifier;
  }

  public Amount amount() {
    return amount;
  }

  public IncomeAttribute incomeAttribute() {
    return incomeAttribute;
  }
}
