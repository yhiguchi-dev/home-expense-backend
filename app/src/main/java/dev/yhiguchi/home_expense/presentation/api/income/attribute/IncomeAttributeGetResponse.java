package dev.yhiguchi.home_expense.presentation.api.income.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;

record IncomeAttributeGetResponse(
    @JsonProperty("id") String id, @JsonProperty("name") String name) {
  static IncomeAttributeGetResponse from(IncomeAttribute incomeAttribute) {
    return new IncomeAttributeGetResponse(
        incomeAttribute.incomeAttributeIdentifier().value(),
        incomeAttribute.incomeAttributeName().value());
  }
}
