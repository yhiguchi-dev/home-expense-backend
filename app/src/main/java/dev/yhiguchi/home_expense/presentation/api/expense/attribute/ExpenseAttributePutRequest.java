package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseAttributeUpdateCommand;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import dev.yhiguchi.home_expense.presentation.validation.ExpenseCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExpenseAttributePutRequest(
    @NotBlank(message = "nameは必須入力です")
        @Size(max = 512, message = "nameは512文字以内で入力してください")
        @JsonProperty("name")
        String name,
    @NotBlank(message = "categoryは必須入力です") @ExpenseCategory @JsonProperty("category")
        String category) {

  ExpenseAttributeUpdateCommand toCommand(String id, long version) {
    return new ExpenseAttributeUpdateCommand(
        new ExpenseAttributeIdentifier(id),
        new ExpenseAttributeName(name),
        dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory.of(category),
        version);
  }
}
