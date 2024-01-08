package dev.yhiguchi.home_expense.presentation.api.income.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeName;
import jakarta.validation.constraints.NotBlank;

public record IncomeAttributePutRequest(
    @NotBlank(message = "nameは必須入力です") @JsonProperty("name") String name) {

  IncomeAttributeName toIncomeAttributeName() {
    return new IncomeAttributeName(name);
  }
}
