package dev.yhiguchi.home_expense.domain.model.expense;

import java.util.Objects;

/** 経費説明 */
public record Description(String value) {
  public Description {
    Objects.requireNonNull(value, "説明は必須です");
  }
}
