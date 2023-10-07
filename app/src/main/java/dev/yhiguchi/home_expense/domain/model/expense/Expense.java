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

  public Expense(
      ExpenseIdentifier expenseIdentifier,
      Description description,
      Price price,
      PaymentDate paymentDate,
      ExpenseAttribute expenseAttribute) {
    this.expenseIdentifier = expenseIdentifier;
    this.description = description;
    this.price = price;
    this.paymentDate = paymentDate;
    this.expenseAttribute = expenseAttribute;
  }

  public Expense() {}

  public boolean isFixed() {
    return expenseAttribute.isFixed();
  }

  public boolean isVariable() {
    return expenseAttribute.isVariable();
  }

  public boolean hasAttribute() {
    return expenseAttribute().exists();
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
