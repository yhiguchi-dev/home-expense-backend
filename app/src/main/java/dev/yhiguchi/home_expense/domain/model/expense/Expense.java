package dev.yhiguchi.home_expense.domain.model.expense;

import dev.yhiguchi.home_expense.domain.model.Amount;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import java.util.Objects;
import java.util.UUID;

/** 経費 */
public class Expense {
  ExpenseIdentifier expenseIdentifier;

  Description description;

  Amount amount;

  PaymentDate paymentDate;

  ExpenseAttributeIdentifier expenseAttributeIdentifier;

  public Expense(
      ExpenseIdentifier expenseIdentifier,
      Description description,
      Amount amount,
      PaymentDate paymentDate,
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    this.expenseIdentifier = expenseIdentifier;
    this.description = description;
    this.amount = amount;
    this.paymentDate = paymentDate;
    this.expenseAttributeIdentifier = expenseAttributeIdentifier;
  }

  public static Expense create(
      Description description,
      Amount amount,
      PaymentDate paymentDate,
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    ExpenseIdentifier id = new ExpenseIdentifier(UUID.randomUUID().toString());
    return new Expense(id, description, amount, paymentDate, expenseAttributeIdentifier);
  }

  public Expense updateWith(
      Description description,
      Amount amount,
      PaymentDate paymentDate,
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    return new Expense(
        this.expenseIdentifier, description, amount, paymentDate, expenseAttributeIdentifier);
  }

  /** 属性値に変更があるか判定する */
  public boolean hasChanges(Expense other) {
    return !Objects.equals(description, other.description)
        || !Objects.equals(amount, other.amount)
        || !Objects.equals(paymentDate, other.paymentDate)
        || !Objects.equals(expenseAttributeIdentifier, other.expenseAttributeIdentifier);
  }

  public ExpenseIdentifier expenseIdentifier() {
    return expenseIdentifier;
  }

  public Description description() {
    return description;
  }

  public Amount amount() {
    return amount;
  }

  public PaymentDate paymentDate() {
    return paymentDate;
  }

  public ExpenseAttributeIdentifier expenseAttributeIdentifier() {
    return expenseAttributeIdentifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Expense expense = (Expense) o;
    return Objects.equals(expenseIdentifier, expense.expenseIdentifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(expenseIdentifier);
  }
}
