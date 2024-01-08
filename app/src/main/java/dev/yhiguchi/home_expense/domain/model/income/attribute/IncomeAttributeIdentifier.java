package dev.yhiguchi.home_expense.domain.model.income.attribute;

import java.util.Objects;

/** 収入属性識別子 */
public class IncomeAttributeIdentifier {
  String value;

  public IncomeAttributeIdentifier(String value) {
    this.value = value;
  }

  IncomeAttributeIdentifier() {}

  public boolean exists() {
    return Objects.nonNull(value);
  }

  public String value() {
    return value;
  }
}
