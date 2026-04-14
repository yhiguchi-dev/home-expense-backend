package dev.yhiguchi.home_expense.presentation.api.income;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.domain.model.income.Amount;
import dev.yhiguchi.home_expense.domain.model.income.Description;
import dev.yhiguchi.home_expense.domain.model.income.ReceiveDate;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record IncomePostRequest(
    @NotBlank(message = "descriptionは必須入力です")
        @Size(max = 512, message = "descriptionは512文字以内で入力してください")
        @JsonProperty("description")
        String description,
    @NotNull(message = "amountは必須入力です")
        @Positive(message = "amountは正の値でなければなりません")
        @JsonProperty("amount")
        Integer amount,
    @NotBlank(message = "receive_dateは必須入力です")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "receive_dateの形式に誤りがあります")
        @JsonProperty("receive_date")
        String receiveDate,
    @NotBlank(message = "attribute_idは必須入力です")
        @Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "attribute_idの形式に誤りがあります")
        @JsonProperty("attribute_id")
        String attributeId) {
  Description toDescription() {
    return new Description(description);
  }

  Amount toAmount() {
    return new Amount(amount);
  }

  ReceiveDate toReceiveDate() {
    return new ReceiveDate(LocalDate.parse(receiveDate));
  }

  IncomeAttributeIdentifier toIncomeAttributeIdentifier() {
    return new IncomeAttributeIdentifier(attributeId);
  }
}
