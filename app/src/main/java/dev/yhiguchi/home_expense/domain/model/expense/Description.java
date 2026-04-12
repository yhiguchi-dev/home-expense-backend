package dev.yhiguchi.home_expense.domain.model.expense;

import java.util.Objects;

/** 経費説明 */
public class Description {
  String value;

  public Description(String value) {
    Objects.requireNonNull(value, "説明は必須です");
    this.value = value;
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Description that = (Description) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
