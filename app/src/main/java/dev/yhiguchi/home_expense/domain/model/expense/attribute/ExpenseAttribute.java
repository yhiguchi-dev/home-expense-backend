package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import java.util.Objects;
import java.util.UUID;

/** 経費属性 */
public class ExpenseAttribute {
  ExpenseAttributeIdentifier expenseAttributeIdentifier;
  ExpenseAttributeName expenseAttributeName;
  ExpenseCategory expenseCategory;
  long version;

  public ExpenseAttribute(
      ExpenseAttributeIdentifier expenseAttributeIdentifier,
      ExpenseAttributeName expenseAttributeName,
      ExpenseCategory expenseCategory) {
    this.expenseAttributeIdentifier = expenseAttributeIdentifier;
    this.expenseAttributeName = expenseAttributeName;
    this.expenseCategory = expenseCategory;
  }

  public ExpenseAttribute(
      ExpenseAttributeIdentifier expenseAttributeIdentifier,
      ExpenseAttributeName expenseAttributeName,
      ExpenseCategory expenseCategory,
      long version) {
    this(expenseAttributeIdentifier, expenseAttributeName, expenseCategory);
    this.version = version;
  }

  public static ExpenseAttribute create(
      ExpenseAttributeName expenseAttributeName, ExpenseCategory expenseCategory) {
    ExpenseAttributeIdentifier expenseAttributeIdentifier =
        new ExpenseAttributeIdentifier(UUID.randomUUID().toString());
    return new ExpenseAttribute(
        expenseAttributeIdentifier, expenseAttributeName, expenseCategory, 1L);
  }

  public ExpenseAttribute updateWith(
      ExpenseAttributeName expenseAttributeName, ExpenseCategory expenseCategory) {
    return new ExpenseAttribute(
        this.expenseAttributeIdentifier, expenseAttributeName, expenseCategory, this.version);
  }

  /** 指定したバージョンを持つExpenseAttributeを返す */
  public ExpenseAttribute withVersion(long version) {
    return new ExpenseAttribute(
        this.expenseAttributeIdentifier, this.expenseAttributeName, this.expenseCategory, version);
  }

  /** 属性値に変更があるか判定する */
  public boolean hasChanges(ExpenseAttribute other) {
    return !Objects.equals(expenseAttributeName, other.expenseAttributeName)
        || expenseCategory != other.expenseCategory;
  }

  public boolean isFixed() {
    return expenseCategory.isFixed();
  }

  public boolean isVariable() {
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

  public long version() {
    return version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExpenseAttribute that = (ExpenseAttribute) o;
    return Objects.equals(expenseAttributeIdentifier, that.expenseAttributeIdentifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(expenseAttributeIdentifier);
  }
}
