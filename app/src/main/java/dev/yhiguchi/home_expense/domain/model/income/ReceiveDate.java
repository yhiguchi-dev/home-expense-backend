package dev.yhiguchi.home_expense.domain.model.income;

import java.time.LocalDate;

/** 受取日 */
public class ReceiveDate {
  LocalDate value;

  public ReceiveDate(LocalDate value) {
    this.value = value;
  }

  public String value() {
    return value.toString();
  }
}
