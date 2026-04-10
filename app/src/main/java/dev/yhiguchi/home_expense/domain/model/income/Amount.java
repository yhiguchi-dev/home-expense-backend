package dev.yhiguchi.home_expense.domain.model.income;

import java.util.Objects;

/** 金額 */
public class Amount {
  int value;

  public Amount(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Amount amount = (Amount) o;
    return value == amount.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
