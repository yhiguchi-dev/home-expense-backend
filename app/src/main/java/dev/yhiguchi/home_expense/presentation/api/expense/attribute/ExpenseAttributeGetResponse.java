package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeDetail;

public record ExpenseAttributeGetResponse(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("category") String category) {
  static ExpenseAttributeGetResponse from(ExpenseAttributeDetail detail) {
    return new ExpenseAttributeGetResponse(detail.id(), detail.name(), detail.category());
  }
}
