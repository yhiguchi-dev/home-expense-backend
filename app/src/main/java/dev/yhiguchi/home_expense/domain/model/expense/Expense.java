package dev.yhiguchi.home_expense.domain.model.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import java.util.Objects;
import java.util.UUID;

/** 経費 */
public class Expense {
  ExpenseIdentifier expenseIdentifier;

  Description description;

  Price price;

  PaymentDate paymentDate;

  ExpenseAttributeIdentifier expenseAttributeIdentifier;

  ExpenseCategory expenseCategory;

  public Expense(
      ExpenseIdentifier expenseIdentifier,
      Description description,
      Price price,
      PaymentDate paymentDate,
      ExpenseAttributeIdentifier expenseAttributeIdentifier,
      ExpenseCategory expenseCategory) {
    this.expenseIdentifier = expenseIdentifier;
    this.description = description;
    this.price = price;
    this.paymentDate = paymentDate;
    this.expenseAttributeIdentifier = expenseAttributeIdentifier;
    this.expenseCategory = expenseCategory;
  }

  public static Expense create(
      Description description,
      Price price,
      PaymentDate paymentDate,
      ExpenseAttributeIdentifier expenseAttributeIdentifier,
      ExpenseCategory expenseCategory) {
    ExpenseIdentifier id = new ExpenseIdentifier(UUID.randomUUID().toString());
    return new Expense(
        id, description, price, paymentDate, expenseAttributeIdentifier, expenseCategory);
  }

  public Expense updateWith(
      Description description,
      Price price,
      PaymentDate paymentDate,
      ExpenseAttributeIdentifier expenseAttributeIdentifier,
      ExpenseCategory expenseCategory) {
    return new Expense(
        this.expenseIdentifier,
        description,
        price,
        paymentDate,
        expenseAttributeIdentifier,
        expenseCategory);
  }

  /** 属性値に変更があるか判定する */
  public boolean hasChanges(Expense other) {
    return !Objects.equals(description, other.description)
        || !Objects.equals(price, other.price)
        || !Objects.equals(paymentDate, other.paymentDate)
        || !Objects.equals(expenseAttributeIdentifier, other.expenseAttributeIdentifier)
        || expenseCategory != other.expenseCategory;
  }

  public boolean isFixed() {
    return expenseCategory.isFixed();
  }

  public boolean isVariable() {
    return expenseCategory.isVariable();
  }

  public ExpenseIdentifier expenseIdentifier() {
    return expenseIdentifier;
  }

  public Description description() {
    return description;
  }

  public Price price() {
    return price;
  }

  public PaymentDate paymentDate() {
    return paymentDate;
  }

  public ExpenseAttributeIdentifier expenseAttributeIdentifier() {
    return expenseAttributeIdentifier;
  }

  public ExpenseCategory expenseCategory() {
    return expenseCategory;
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
