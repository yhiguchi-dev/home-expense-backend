package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseAttributeRegistrationCommand;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import dev.yhiguchi.home_expense.presentation.validation.ExpenseCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExpenseAttributePostRequest(
    @NotBlank(message = "nameは必須入力です")
        @Size(max = ExpenseAttributeName.MAX_LENGTH, message = "nameは{max}文字以内で入力してください")
        @JsonProperty("name")
        String name,
    @NotBlank(message = "categoryは必須入力です") @ExpenseCategory @JsonProperty("category")
        String category) {

  ExpenseAttributeRegistrationCommand toCommand() {
    return new ExpenseAttributeRegistrationCommand(
        new ExpenseAttributeName(name),
        dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory.of(category));
  }
}
