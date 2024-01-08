package dev.yhiguchi.home_expense.domain.model.income.attribute;

/** 収入属性 */
public class IncomeAttribute {
  IncomeAttributeIdentifier incomeAttributeIdentifier;

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
}
