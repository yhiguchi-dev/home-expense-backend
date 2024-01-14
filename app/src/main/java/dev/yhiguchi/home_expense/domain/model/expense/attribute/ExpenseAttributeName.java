package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import java.util.Objects;

/** 経費属性名 */
public class ExpenseAttributeName {
  String value;

  public ExpenseAttributeName(String value) {
    this.value = value;
  }

  public ExpenseAttributeName() {}

  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExpenseAttributeName that = (ExpenseAttributeName) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
