package dev.yhiguchi.home_expense.domain.model.income;

import java.util.Objects;

/** 収入説明 */
public record Description(String value) {
  public Description {
    Objects.requireNonNull(value, "説明は必須です");
  }
}
