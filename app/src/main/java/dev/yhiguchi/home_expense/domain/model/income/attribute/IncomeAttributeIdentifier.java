package dev.yhiguchi.home_expense.domain.model.income.attribute;

import java.util.Objects;

/** 収入属性識別子 */
public class IncomeAttributeIdentifier {
  String value;

  public IncomeAttributeIdentifier(String value) {
    this.value = value;
  }

  IncomeAttributeIdentifier() {}

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
    IncomeAttributeIdentifier that = (IncomeAttributeIdentifier) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
