package dev.yhiguchi.home_expense.domain.model.income.attribute;

import java.util.Objects;
import java.util.UUID;

/** 収入属性 */
public class IncomeAttribute {
  IncomeAttributeIdentifier incomeAttributeIdentifier;

  IncomeAttributeName incomeAttributeName;

  long version;

  public IncomeAttribute(
      IncomeAttributeIdentifier incomeAttributeIdentifier,
      IncomeAttributeName incomeAttributeName,
      long version) {
    this.incomeAttributeIdentifier = incomeAttributeIdentifier;
    this.incomeAttributeName = incomeAttributeName;
    this.version = version;
  }

  public static IncomeAttribute create(IncomeAttributeName incomeAttributeName) {
    IncomeAttributeIdentifier incomeAttributeIdentifier =
        new IncomeAttributeIdentifier(UUID.randomUUID().toString());
    return new IncomeAttribute(incomeAttributeIdentifier, incomeAttributeName, 1L);
  }

  public IncomeAttribute updateWith(IncomeAttributeName incomeAttributeName) {
    return new IncomeAttribute(this.incomeAttributeIdentifier, incomeAttributeName, this.version);
  }

  /** 指定したバージョンを持つIncomeAttributeを返す */
  public IncomeAttribute withVersion(long version) {
    return new IncomeAttribute(this.incomeAttributeIdentifier, this.incomeAttributeName, version);
  }

  /** 属性値に変更があるか判定する */
  public boolean hasChanges(IncomeAttribute other) {
    return !Objects.equals(incomeAttributeName, other.incomeAttributeName);
  }

  /** 指定した名前が現在の名前と同一か判定する */
  public boolean hasSameName(IncomeAttributeName incomeAttributeName) {
    return Objects.equals(this.incomeAttributeName, incomeAttributeName);
  }

  public IncomeAttributeIdentifier incomeAttributeIdentifier() {
    return incomeAttributeIdentifier;
  }

  public IncomeAttributeName incomeAttributeName() {
    return incomeAttributeName;
  }

  public long version() {
    return version;
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
