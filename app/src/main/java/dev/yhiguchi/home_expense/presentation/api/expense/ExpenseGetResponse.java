package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.ExpenseDetail;

public record ExpenseGetResponse(
    @JsonProperty("id") String id,
    @JsonProperty("description") String description,
    @JsonProperty("amount") Integer amount,
    @JsonProperty("payment_date") String paymentDate,
    @JsonProperty("expense_attribute_id") String expenseAttributeId,
    @JsonProperty("expense_category") String expenseCategory) {

  static ExpenseGetResponse from(ExpenseDetail detail) {
    return new ExpenseGetResponse(
        detail.expense().expenseIdentifier().value(),
        detail.expense().description().value(),
        detail.expense().amount().value(),
        detail.expense().paymentDate().asString(),
        detail.expense().expenseAttributeIdentifier().value(),
        detail.attribute().expenseCategory().name());
  }
}
