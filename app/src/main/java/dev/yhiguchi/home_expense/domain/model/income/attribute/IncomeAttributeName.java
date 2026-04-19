package dev.yhiguchi.home_expense.domain.model.income.attribute;

import java.util.Objects;

/** 収入属性名 */
public record IncomeAttributeName(String value) {
  public IncomeAttributeName {
    Objects.requireNonNull(value, "収入属性名は必須です");
  }
}
