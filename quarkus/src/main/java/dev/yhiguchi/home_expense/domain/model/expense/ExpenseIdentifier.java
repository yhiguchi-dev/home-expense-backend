package dev.yhiguchi.home_expense.domain.model.expense;

import java.util.Objects;

/** 経費識別子 */
public class ExpenseIdentifier {
  String value;

  public ExpenseIdentifier(String value) {
    this.value = value;
  }

  ExpenseIdentifier() {}

  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExpenseIdentifier that = (ExpenseIdentifier) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
