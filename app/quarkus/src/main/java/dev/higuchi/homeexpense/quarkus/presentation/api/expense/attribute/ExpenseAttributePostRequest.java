package dev.higuchi.homeexpense.quarkus.presentation.api.expense.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeName;
import dev.higuchi.homeexpense.quarkus.presentation.validation.ExpenseCategory;
import jakarta.validation.constraints.NotBlank;

record ExpenseAttributePostRequest(
    @NotBlank(message = "nameは必須入力です") @JsonProperty("name") String name,
    @NotBlank(message = "categoryは必須入力です") @ExpenseCategory @JsonProperty("category")
        String category) {

  ExpenseAttributeName toExpenseAttributeName() {
    return new ExpenseAttributeName(name);
  }

  dev.higuchi.homeexpense.command.model.expense.ExpenseCategory toExpenseCategory() {
    return dev.higuchi.homeexpense.command.model.expense.ExpenseCategory.of(category);
  }
}
