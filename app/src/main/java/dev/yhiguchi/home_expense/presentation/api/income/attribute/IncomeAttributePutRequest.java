package dev.yhiguchi.home_expense.presentation.api.income.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.application.usecase.income.IncomeAttributeUpdateCommand;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IncomeAttributePutRequest(
    @NotBlank(message = "nameは必須入力です")
        @Size(max = 512, message = "nameは512文字以内で入力してください")
        @JsonProperty("name")
        String name) {

  IncomeAttributeUpdateCommand toCommand(String id, long version) {
    return new IncomeAttributeUpdateCommand(
        new IncomeAttributeIdentifier(id), new IncomeAttributeName(name), version);
  }
}
