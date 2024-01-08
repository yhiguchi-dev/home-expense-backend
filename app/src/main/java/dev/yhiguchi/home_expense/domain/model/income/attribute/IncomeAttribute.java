package dev.yhiguchi.home_expense.domain.model.income.attribute;

import java.util.Objects;

/** 収入属性 */
public class IncomeAttribute {
  IncomeAttributeIdentifier incomeAttributeIdentifier = new IncomeAttributeIdentifier();

  IncomeAttributeName incomeAttributeName;

  public IncomeAttribute(
      IncomeAttributeIdentifier incomeAttributeIdentifier,
      IncomeAttributeName incomeAttributeName) {
    this.incomeAttributeIdentifier = incomeAttributeIdentifier;
    this.incomeAttributeName = incomeAttributeName;
  }

  public IncomeAttribute() {}

  public boolean exists() {
    return incomeAttributeIdentifier().exists();
  }

  public IncomeAttributeIdentifier incomeAttributeIdentifier() {
    return incomeAttributeIdentifier;
  }

  public IncomeAttributeName incomeAttributeName() {
    return incomeAttributeName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    IncomeAttribute that = (IncomeAttribute) o;
    return Objects.equals(incomeAttributeIdentifier, that.incomeAttributeIdentifier)
        && Objects.equals(incomeAttributeName, that.incomeAttributeName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(incomeAttributeIdentifier, incomeAttributeName);
  }
}
