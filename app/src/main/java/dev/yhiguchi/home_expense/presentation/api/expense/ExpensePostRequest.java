package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseRegistrationCommand;
import dev.yhiguchi.home_expense.domain.model.expense.Description;
import dev.yhiguchi.home_expense.domain.model.expense.PaymentDate;
import dev.yhiguchi.home_expense.domain.model.expense.Price;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ExpensePostRequest(
    @NotBlank(message = "descriptionは必須入力です")
        @Size(max = 512, message = "descriptionは512文字以内で入力してください")
        @JsonProperty("description")
        String description,
    @NotNull(message = "priceは必須入力です")
        @Positive(message = "priceは正の値でなければなりません")
        @JsonProperty("price")
        Integer price,
    @NotBlank(message = "payment_dateは必須入力です")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "payment_dateの形式に誤りがあります")
        @JsonProperty("payment_date")
        String paymentDate,
    @NotBlank(message = "attribute_idは必須入力です")
        @Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "attribute_idの形式に誤りがあります")
        @JsonProperty("attribute_id")
        String attributeId) {

  ExpenseRegistrationCommand toCommand() {
    return new ExpenseRegistrationCommand(
        new Description(description),
        new Price(price),
        new PaymentDate(LocalDate.parse(paymentDate)),
        new ExpenseAttributeIdentifier(attributeId));
  }
}
