package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
record ExpenseGetResponse(
    @JsonProperty("id") String id,
    @JsonProperty("description") String description,
    @JsonProperty("price") Integer price,
    @JsonProperty("payment_date") String paymentDate,
    @JsonProperty("expense_attribute") ExpenseAttributeResponse expenseAttributeResponse) {

  static ExpenseGetResponse from(Expense expense) {
    ExpenseAttributeResponse expenseAttributeResponse =
        expense.hasAttribute()
            ? new ExpenseAttributeResponse(
                expense.expenseAttribute().expenseAttributeIdentifier().value(),
                expense.expenseAttribute().expenseAttributeName().value(),
                expense.expenseAttribute().expenseCategory().name())
            : null;
    return new ExpenseGetResponse(
        expense.expenseIdentifier().value(),
        expense.description().value(),
        expense.price().value(),
        expense.paymentDate().value(),
        expenseAttributeResponse);
  }
}
