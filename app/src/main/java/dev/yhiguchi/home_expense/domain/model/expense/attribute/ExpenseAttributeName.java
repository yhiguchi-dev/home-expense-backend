package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import java.util.Objects;

/** 経費属性名 */
public record ExpenseAttributeName(String value) {
  public static final int MAX_LENGTH = 512;

  public ExpenseAttributeName {
    Objects.requireNonNull(value, "経費属性名は必須です");
    if (value.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("経費属性名は" + MAX_LENGTH + "文字以内でなければなりません");
    }
  }
}
