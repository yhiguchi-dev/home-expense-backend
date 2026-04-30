package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import java.util.Objects;

/** 経費属性識別子 */
public record ExpenseAttributeIdentifier(String value) {
  public ExpenseAttributeIdentifier {
    Objects.requireNonNull(value, "経費属性識別子は必須です");
  }
}
