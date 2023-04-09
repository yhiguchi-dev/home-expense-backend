package dev.yhiguchi.home_expense.domain.model.expense;

import java.time.LocalDate;
import java.util.Objects;

/** 支払日 */
public class PaymentDate {
  LocalDate value;

  public PaymentDate(LocalDate value) {
    this.value = value;
  }

  PaymentDate() {}

  public String value() {
    return value.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PaymentDate that = (PaymentDate) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
