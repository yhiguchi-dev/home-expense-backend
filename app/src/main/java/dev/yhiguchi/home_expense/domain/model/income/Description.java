package dev.yhiguchi.home_expense.domain.model.income;

/** 収入説明 */
public class Description {
  String value;

  public Description(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}
