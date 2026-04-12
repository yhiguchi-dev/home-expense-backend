package dev.yhiguchi.home_expense.domain.model.income;

import java.time.LocalDate;
import java.util.Objects;

/** 受取日 */
public class ReceiveDate {
  LocalDate value;

  public ReceiveDate(LocalDate value) {
    Objects.requireNonNull(value, "受取日は必須です");
    this.value = value;
  }

  public String value() {
    return value.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReceiveDate that = (ReceiveDate) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
