package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import java.util.Objects;
import java.util.UUID;

/** 経費属性 */
public class ExpenseAttribute {
  ExpenseAttributeIdentifier expenseAttributeIdentifier;
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

  public static ExpenseAttribute create(
      ExpenseAttributeName expenseAttributeName, ExpenseCategory expenseCategory) {
    ExpenseAttributeIdentifier expenseAttributeIdentifier =
        new ExpenseAttributeIdentifier(UUID.randomUUID().toString());
    return new ExpenseAttribute(expenseAttributeIdentifier, expenseAttributeName, expenseCategory);
  }

  /** 名前のみ更新する。category は登録後に変更不可。 */
  public ExpenseAttribute updateWith(ExpenseAttributeName expenseAttributeName) {
    return new ExpenseAttribute(
        this.expenseAttributeIdentifier, expenseAttributeName, this.expenseCategory);
  }

  /** 属性値に変更があるか判定する */
  public boolean hasChanges(ExpenseAttribute other) {
    return !Objects.equals(expenseAttributeName, other.expenseAttributeName);
  }

  /** 指定した名前が現在の名前と同一か判定する */
  public boolean hasSameName(ExpenseAttributeName expenseAttributeName) {
    return Objects.equals(this.expenseAttributeName, expenseAttributeName);
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
