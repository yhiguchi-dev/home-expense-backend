package dev.yhiguchi.home_expense.domain.model.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import java.util.Objects;

/** 経費 */
public class Expense {
  ExpenseIdentifier expenseIdentifier;

  Description description;

  Price price;

  PaymentDate paymentDate;

  ExpenseAttribute expenseAttribute = new ExpenseAttribute();

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

  public Expense() {}

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
    return Objects.equals(expenseIdentifier, expense.expenseIdentifier)
        && Objects.equals(description, expense.description)
        && Objects.equals(price, expense.price)
        && Objects.equals(paymentDate, expense.paymentDate)
        && Objects.equals(expenseAttribute, expense.expenseAttribute);
  }

  @Override
  public int hashCode() {
    return Objects.hash(expenseIdentifier, description, price, paymentDate, expenseAttribute);
  }
}
