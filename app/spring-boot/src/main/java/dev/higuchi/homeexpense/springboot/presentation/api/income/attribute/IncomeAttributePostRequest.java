package dev.higuchi.homeexpense.springboot.presentation.api.income.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeName;
import jakarta.validation.constraints.NotBlank;

record IncomeAttributePostRequest(
    @NotBlank(message = "nameは必須入力です") @JsonProperty("name") String name) {

  IncomeAttributeName toIncomeAttributeName() {
    return new IncomeAttributeName(name);
  }
}
