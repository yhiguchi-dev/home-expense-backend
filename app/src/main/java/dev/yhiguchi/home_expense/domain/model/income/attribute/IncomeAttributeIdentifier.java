package dev.yhiguchi.home_expense.domain.model.income.attribute;

/** 収入属性識別子 */
public class IncomeAttributeIdentifier {
  String value;

  public IncomeAttributeIdentifier(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}
