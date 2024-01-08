package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;

record ExpenseAttributeResponse(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("category") String category) {

  static ExpenseAttributeResponse from(ExpenseAttribute expenseAttribute) {
    return new ExpenseAttributeResponse(
        expenseAttribute.expenseAttributeIdentifier().value(),
        expenseAttribute.expenseAttributeName().value(),
        expenseAttribute.expenseCategory().name());
  }
}
