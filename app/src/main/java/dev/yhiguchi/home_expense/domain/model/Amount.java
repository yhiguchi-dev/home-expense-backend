package dev.yhiguchi.home_expense.domain.model;

/** 金額（経費・収入で共通） */
public record Amount(int value) {
  public static final int MAX_VALUE = 1_000_000_000;

  public Amount {
    if (value <= 0) {
      throw new IllegalArgumentException("金額は正の値でなければなりません");
    }
    if (value > MAX_VALUE) {
      throw new IllegalArgumentException("金額は" + MAX_VALUE + "以下でなければなりません");
    }
  }
}
