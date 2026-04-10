package dev.yhiguchi.home_expense.domain.model.income;

import java.util.Objects;

/** 収入識別子 */
public class IncomeIdentifier {
  String value;

  public IncomeIdentifier(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    IncomeIdentifier that = (IncomeIdentifier) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
