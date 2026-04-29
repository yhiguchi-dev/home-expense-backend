package dev.yhiguchi.home_expense.domain.model.income.attribute;

import java.util.Objects;

/** 収入属性名 */
public record IncomeAttributeName(String value) {
  public static final int MAX_LENGTH = 512;

  public IncomeAttributeName {
    Objects.requireNonNull(value, "収入属性名は必須です");
    if (value.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("収入属性名は" + MAX_LENGTH + "文字以内でなければなりません");
    }
  }
}
