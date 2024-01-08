package dev.yhiguchi.home_expense.presentation.api.income;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.domain.model.income.Amount;
import dev.yhiguchi.home_expense.domain.model.income.Description;
import dev.yhiguchi.home_expense.domain.model.income.ReceiveDate;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record IncomePostRequest(
    @NotNull(message = "descriptionは必須入力です") @JsonProperty("description") String description,
    @NotNull(message = "amountは必須入力です") @JsonProperty("amount") Integer amount,
    @NotBlank(message = "receive_dateは必須入力です")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}", message = "receive_dateの形式に誤りがあります")
        @JsonProperty("receive_date")
        String paymentDate,
    @NotBlank(message = "attribute_idは必須入力です") @JsonProperty("attribute_id") String attributeId) {
  Description toDescription() {
    return new Description(description);
  }

  Amount toAmount() {
    return new Amount(amount);
  }

  ReceiveDate toReceiveDate() {
    return new ReceiveDate(LocalDate.parse(paymentDate));
  }

  IncomeAttributeIdentifier toIncomeAttributeIdentifier() {
    return new IncomeAttributeIdentifier(attributeId);
  }
}
