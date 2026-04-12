package dev.yhiguchi.home_expense.domain.model.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import java.util.Objects;

/** 収入 */
public class Income {
  IncomeIdentifier incomeIdentifier;
  Description description;
  Amount amount;
  ReceiveDate receiveDate;

  IncomeAttribute incomeAttribute;

  long version;

  public Income(
      IncomeIdentifier incomeIdentifier,
      Description description,
      Amount amount,
      ReceiveDate receiveDate,
      IncomeAttribute incomeAttribute,
      long version) {
    this.incomeIdentifier = incomeIdentifier;
    this.description = description;
    this.amount = amount;
    this.receiveDate = receiveDate;
    this.incomeAttribute = incomeAttribute;
    this.version = version;
  }

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

  public long version() {
    return version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Income income = (Income) o;
    return Objects.equals(incomeIdentifier, income.incomeIdentifier)
        && Objects.equals(description, income.description)
        && Objects.equals(amount, income.amount)
        && Objects.equals(receiveDate, income.receiveDate)
        && Objects.equals(incomeAttribute, income.incomeAttribute);
  }

  @Override
  public int hashCode() {
    return Objects.hash(incomeIdentifier, description, amount, receiveDate, incomeAttribute);
  }
}
