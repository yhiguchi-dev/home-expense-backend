package dev.yhiguchi.home_expense.domain.model.income;

/** 金額 */
public record Amount(int value) {
  public Amount {
    if (value <= 0) {
      throw new IllegalArgumentException("金額は正の値でなければなりません");
    }
  }
}
