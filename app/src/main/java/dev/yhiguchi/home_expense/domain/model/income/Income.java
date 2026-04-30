package dev.yhiguchi.home_expense.domain.model.income;

import dev.yhiguchi.home_expense.domain.model.Amount;
import dev.yhiguchi.home_expense.domain.model.Description;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import java.util.Objects;
import java.util.UUID;

/** 収入 */
public class Income {
  IncomeIdentifier incomeIdentifier;
  Description description;
  Amount amount;
  ReceiveDate receiveDate;

  IncomeAttributeIdentifier incomeAttributeIdentifier;

  public Income(
      IncomeIdentifier incomeIdentifier,
      Description description,
      Amount amount,
      ReceiveDate receiveDate,
      IncomeAttributeIdentifier incomeAttributeIdentifier) {
    this.incomeIdentifier = Objects.requireNonNull(incomeIdentifier, "収入識別子は必須です");
    this.description = Objects.requireNonNull(description, "説明は必須です");
    this.amount = Objects.requireNonNull(amount, "金額は必須です");
    this.receiveDate = Objects.requireNonNull(receiveDate, "受取日は必須です");
    this.incomeAttributeIdentifier =
        Objects.requireNonNull(incomeAttributeIdentifier, "収入属性識別子は必須です");
  }

  public static Income create(
      Description description,
      Amount amount,
      ReceiveDate receiveDate,
      IncomeAttributeIdentifier incomeAttributeIdentifier) {
    IncomeIdentifier id = new IncomeIdentifier(UUID.randomUUID().toString());
    return new Income(id, description, amount, receiveDate, incomeAttributeIdentifier);
  }

  public Income updateWith(
      Description description,
      Amount amount,
      ReceiveDate receiveDate,
      IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return new Income(
        this.incomeIdentifier, description, amount, receiveDate, incomeAttributeIdentifier);
  }

  public boolean hasChanges(Income other) {
    return !Objects.equals(description, other.description)
        || !Objects.equals(amount, other.amount)
        || !Objects.equals(receiveDate, other.receiveDate)
        || !Objects.equals(incomeAttributeIdentifier, other.incomeAttributeIdentifier);
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

  public IncomeAttributeIdentifier incomeAttributeIdentifier() {
    return incomeAttributeIdentifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Income income = (Income) o;
    return Objects.equals(incomeIdentifier, income.incomeIdentifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(incomeIdentifier);
  }
}
