package dev.yhiguchi.home_expense.domain.model.income.attribute;

import java.util.Objects;
import java.util.UUID;

/** 収入属性 */
public class IncomeAttribute {
  IncomeAttributeIdentifier incomeAttributeIdentifier;

  IncomeAttributeName incomeAttributeName;

  public IncomeAttribute(
      IncomeAttributeIdentifier incomeAttributeIdentifier,
      IncomeAttributeName incomeAttributeName) {
    this.incomeAttributeIdentifier =
        Objects.requireNonNull(incomeAttributeIdentifier, "収入属性識別子は必須です");
    this.incomeAttributeName = Objects.requireNonNull(incomeAttributeName, "収入属性名は必須です");
  }

  public static IncomeAttribute create(IncomeAttributeName incomeAttributeName) {
    IncomeAttributeIdentifier incomeAttributeIdentifier =
        new IncomeAttributeIdentifier(UUID.randomUUID().toString());
    return new IncomeAttribute(incomeAttributeIdentifier, incomeAttributeName);
  }

  public IncomeAttribute updateWith(IncomeAttributeName incomeAttributeName) {
    return new IncomeAttribute(this.incomeAttributeIdentifier, incomeAttributeName);
  }

  public boolean hasChanges(IncomeAttribute other) {
    return !Objects.equals(incomeAttributeName, other.incomeAttributeName);
  }

  public boolean hasSameName(IncomeAttributeName incomeAttributeName) {
    return Objects.equals(this.incomeAttributeName, incomeAttributeName);
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
    return Objects.equals(incomeAttributeIdentifier, that.incomeAttributeIdentifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(incomeAttributeIdentifier);
  }
}
