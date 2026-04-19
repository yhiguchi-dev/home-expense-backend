package dev.yhiguchi.home_expense.domain.model.expense;

/** 金額 */
public record Price(int value) {
  public Price {
    if (value <= 0) {
      throw new IllegalArgumentException("金額は正の値でなければなりません");
    }
  }
}
