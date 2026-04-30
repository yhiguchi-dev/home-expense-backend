package dev.yhiguchi.home_expense.domain.model.income;

import java.util.Objects;

/** 収入識別子 */
public record IncomeIdentifier(String value) {
  public IncomeIdentifier {
    Objects.requireNonNull(value, "収入識別子は必須です");
  }
}
