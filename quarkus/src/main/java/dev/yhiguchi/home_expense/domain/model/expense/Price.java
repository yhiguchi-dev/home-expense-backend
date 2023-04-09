package dev.yhiguchi.home_expense.domain.model.expense;

import java.util.Objects;

/** 金額 */
public class Price {
  int value;

  public Price(int value) {
    this.value = value;
  }

  Price() {}

  public int value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Price price = (Price) o;
    return value == price.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
