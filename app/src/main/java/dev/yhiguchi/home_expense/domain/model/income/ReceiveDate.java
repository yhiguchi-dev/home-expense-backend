package dev.yhiguchi.home_expense.domain.model.income;

import java.time.LocalDate;
import java.util.Objects;

/** 受取日 */
public record ReceiveDate(LocalDate value) {
  public ReceiveDate {
    Objects.requireNonNull(value, "受取日は必須です");
  }

  public String asString() {
    return value.toString();
  }
}
