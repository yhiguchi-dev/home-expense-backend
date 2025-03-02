package dev.higuchi.homeexpense.quarkus.presentation.api.income.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;

record IncomeAttributeGetResponse(
    @JsonProperty("id") String id, @JsonProperty("name") String name) {
  static IncomeAttributeGetResponse from(IncomeAttribute incomeAttribute) {
    return new IncomeAttributeGetResponse(
        incomeAttribute.incomeAttributeIdentifier().value(),
        incomeAttribute.incomeAttributeName().value());
  }
}
