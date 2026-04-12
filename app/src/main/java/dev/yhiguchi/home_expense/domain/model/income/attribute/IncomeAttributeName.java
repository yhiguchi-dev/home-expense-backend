package dev.yhiguchi.home_expense.domain.model.income.attribute;

import java.util.Objects;

/** 収入属性名 */
public class IncomeAttributeName {
  String value;

  public IncomeAttributeName(String value) {
    Objects.requireNonNull(value, "収入属性名は必須です");
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    IncomeAttributeName that = (IncomeAttributeName) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  public String value() {
    return value;
  }
}
