package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import java.util.Objects;

/** 経費属性名 */
public record ExpenseAttributeName(String value) {
  public ExpenseAttributeName {
    Objects.requireNonNull(value, "経費属性名は必須です");
  }
}
