package dev.yhiguchi.home_expense.domain.model.income;

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

  long version;

  public Income(
      IncomeIdentifier incomeIdentifier,
      Description description,
      Amount amount,
      ReceiveDate receiveDate,
      IncomeAttributeIdentifier incomeAttributeIdentifier,
      long version) {
    this.incomeIdentifier = incomeIdentifier;
    this.description = description;
    this.amount = amount;
    this.receiveDate = receiveDate;
    this.incomeAttributeIdentifier = incomeAttributeIdentifier;
    this.version = version;
  }

  public static Income create(
      Description description,
      Amount amount,
      ReceiveDate receiveDate,
      IncomeAttributeIdentifier incomeAttributeIdentifier) {
    IncomeIdentifier id = new IncomeIdentifier(UUID.randomUUID().toString());
    return new Income(id, description, amount, receiveDate, incomeAttributeIdentifier, 1L);
  }

  public Income updateWith(
      Description description,
      Amount amount,
      ReceiveDate receiveDate,
      IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return new Income(
        this.incomeIdentifier,
        description,
        amount,
        receiveDate,
        incomeAttributeIdentifier,
        this.version);
  }

  /** 指定したバージョンを持つIncomeを返す */
  public Income withVersion(long version) {
    return new Income(
        this.incomeIdentifier,
        this.description,
        this.amount,
        this.receiveDate,
        this.incomeAttributeIdentifier,
        version);
  }

  /** 属性値に変更があるか判定する */
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

  public long version() {
    return version;
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
