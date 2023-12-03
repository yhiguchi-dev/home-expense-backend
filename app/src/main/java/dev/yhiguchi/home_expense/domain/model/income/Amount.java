package dev.yhiguchi.home_expense.domain.model.income;

/** 金額 */
public class Amount {
  int value;

  public Amount(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
