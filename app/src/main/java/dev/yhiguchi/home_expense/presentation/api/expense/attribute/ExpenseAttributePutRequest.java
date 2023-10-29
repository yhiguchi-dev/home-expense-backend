package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import dev.yhiguchi.home_expense.presentation.validation.ExpenseCategory;
import jakarta.validation.constraints.NotBlank;

public record ExpenseAttributePutRequest(
    @NotBlank(message = "nameは必須入力です") @JsonProperty("name") String name,
    @NotBlank(message = "categoryは必須入力です") @ExpenseCategory @JsonProperty("category")
        String category) {

  ExpenseAttributeName toExpenseAttributeName() {
    return new ExpenseAttributeName(name);
  }

  dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory toExpenseCategory() {
    return dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory.of(category);
  }
}
