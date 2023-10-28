package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;

record ExpenseAttributeGetResponse(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("category") String category) {
  static ExpenseAttributeGetResponse from(ExpenseAttribute expenseAttribute) {
    return new ExpenseAttributeGetResponse(
        expenseAttribute.expenseAttributeIdentifier().value(),
        expenseAttribute.expenseAttributeName().value(),
        expenseAttribute.expenseCategory().name());
  }
}
