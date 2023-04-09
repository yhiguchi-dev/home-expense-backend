package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import java.util.Objects;

/** 経費属性 */
public class ExpenseAttribute {
  ExpenseAttributeIdentifier expenseAttributeIdentifier = new ExpenseAttributeIdentifier();
  ExpenseAttributeName expenseAttributeName;
  ExpenseCategory expenseCategory;

  public ExpenseAttribute(
      ExpenseAttributeIdentifier expenseAttributeIdentifier,
      ExpenseAttributeName expenseAttributeName,
      ExpenseCategory expenseCategory) {
    this.expenseAttributeIdentifier = expenseAttributeIdentifier;
    this.expenseAttributeName = expenseAttributeName;
    this.expenseCategory = expenseCategory;
  }

  public ExpenseAttribute() {}

  public boolean exists() {
    return expenseAttributeIdentifier.exists();
  }

  public boolean isFixed() {
    if (!exists()) {
      return false;
    }
    return expenseCategory.isFixed();
  }

  public boolean isVariable() {
    if (!exists()) {
      return false;
    }
    return expenseCategory.isVariable();
  }

  public ExpenseAttributeIdentifier expenseAttributeIdentifier() {
    return expenseAttributeIdentifier;
  }

  public ExpenseAttributeName expenseAttributeName() {
    return expenseAttributeName;
  }

  public ExpenseCategory expenseCategory() {
    return expenseCategory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExpenseAttribute that = (ExpenseAttribute) o;
    return Objects.equals(expenseAttributeIdentifier, that.expenseAttributeIdentifier)
        && Objects.equals(expenseAttributeName, that.expenseAttributeName)
        && expenseCategory == that.expenseCategory;
  }

  @Override
  public int hashCode() {
    return Objects.hash(expenseAttributeIdentifier, expenseAttributeName, expenseCategory);
  }
}
