package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import java.util.Objects;

/** 経費属性識別子 */
public class ExpenseAttributeIdentifier {
  String value;

  public ExpenseAttributeIdentifier(String value) {
    this.value = value;
  }

  ExpenseAttributeIdentifier() {}

  public boolean exists() {
    return Objects.nonNull(value);
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExpenseAttributeIdentifier that = (ExpenseAttributeIdentifier) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
