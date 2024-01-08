package dev.yhiguchi.home_expense.domain.model.income;

/** 収入識別子 */
public class IncomeIdentifier {
  String value;

  public IncomeIdentifier(String value) {
    this.value = value;
  }

  IncomeIdentifier() {}

  public String value() {
    return value;
  }
}
