package dev.yhiguchi.home_expense.presentation.api.income.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeDetail;

public record IncomeAttributeGetResponse(
    @JsonProperty("id") String id, @JsonProperty("name") String name) {
  static IncomeAttributeGetResponse from(IncomeAttributeDetail detail) {
    return new IncomeAttributeGetResponse(detail.id(), detail.name());
  }
}
