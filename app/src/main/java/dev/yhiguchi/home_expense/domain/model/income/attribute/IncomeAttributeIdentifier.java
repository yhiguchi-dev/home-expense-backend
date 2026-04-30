package dev.yhiguchi.home_expense.domain.model.income.attribute;

import java.util.Objects;

/** 収入属性識別子 */
public record IncomeAttributeIdentifier(String value) {
  public IncomeAttributeIdentifier {
    Objects.requireNonNull(value, "収入属性識別子は必須です");
  }
}
