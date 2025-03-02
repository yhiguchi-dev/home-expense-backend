package dev.higuchi.homeexpense.springboot.presentation.api.income;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;

record IncomeAttributeResponse(@JsonProperty("id") String id, @JsonProperty("name") String name) {

  static IncomeAttributeResponse from(IncomeAttribute incomeAttribute) {
    return new IncomeAttributeResponse(
        incomeAttribute.incomeAttributeIdentifier().value(),
        incomeAttribute.incomeAttributeName().value());
  }
}
