package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.domain.model.expense.Expense;

public record ExpenseGetResponse(
    @JsonProperty("id") String id,
    @JsonProperty("description") String description,
    @JsonProperty("price") Integer price,
    @JsonProperty("payment_date") String paymentDate,
    @JsonProperty("expense_attribute_id") String expenseAttributeId,
    @JsonProperty("expense_category") String expenseCategory) {

  static ExpenseGetResponse from(Expense expense) {
    return new ExpenseGetResponse(
        expense.expenseIdentifier().value(),
        expense.description().value(),
        expense.price().value(),
        expense.paymentDate().asString(),
        expense.expenseAttributeIdentifier().value(),
        expense.expenseCategory().name());
  }
}
