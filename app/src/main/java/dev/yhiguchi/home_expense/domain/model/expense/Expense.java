package dev.yhiguchi.home_expense.domain.model.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import java.util.Objects;
import java.util.UUID;

/** 経費 */
public class Expense {
  ExpenseIdentifier expenseIdentifier;

  Description description;

  Price price;

  PaymentDate paymentDate;

  ExpenseAttribute expenseAttribute;

  long version;

  public Expense(
      ExpenseIdentifier expenseIdentifier,
      Description description,
      Price price,
      PaymentDate paymentDate,
      ExpenseAttribute expenseAttribute,
      long version) {
    this.expenseIdentifier = expenseIdentifier;
    this.description = description;
    this.price = price;
    this.paymentDate = paymentDate;
    this.expenseAttribute = expenseAttribute;
    this.version = version;
  }

  public static Expense create(
      Description description,
      Price price,
      PaymentDate paymentDate,
      ExpenseAttribute expenseAttribute) {
    ExpenseIdentifier expenseIdentifier = new ExpenseIdentifier(UUID.randomUUID().toString());
    return new Expense(expenseIdentifier, description, price, paymentDate, expenseAttribute, 1L);
  }

  public Expense updateWith(
      Description description,
      Price price,
      PaymentDate paymentDate,
      ExpenseAttribute expenseAttribute) {
    return new Expense(
        this.expenseIdentifier, description, price, paymentDate, expenseAttribute, this.version);
  }

  /** 指定したバージョンを持つExpenseを返す */
  public Expense withVersion(long version) {
    return new Expense(
        this.expenseIdentifier,
        this.description,
        this.price,
        this.paymentDate,
        this.expenseAttribute,
        version);
  }

  /** 属性値に変更があるか判定する */
  public boolean hasChanges(Expense other) {
    return !Objects.equals(description, other.description)
        || !Objects.equals(price, other.price)
        || !Objects.equals(paymentDate, other.paymentDate)
        || !Objects.equals(expenseAttribute, other.expenseAttribute);
  }

  public boolean isFixed() {
    return expenseAttribute.isFixed();
  }

  public boolean isVariable() {
    return expenseAttribute.isVariable();
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

  public ExpenseAttribute expenseAttribute() {
    return expenseAttribute;
  }

  public long version() {
    return version;
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
