package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.domain.model.expense.Description;
import dev.yhiguchi.home_expense.domain.model.expense.PaymentDate;
import dev.yhiguchi.home_expense.domain.model.expense.Price;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record ExpensePostRequest(
    @NotNull(message = "descriptionは必須入力です") @JsonProperty("description") String description,
    @NotNull(message = "priceは必須入力です") @JsonProperty("price") Integer price,
    @NotBlank(message = "payment_dateは必須入力です")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}", message = "payment_dateの形式に誤りがあります")
        @JsonProperty("payment_date")
        String paymentDate,
    @JsonProperty("attribute_id") String attributeId) {
  Description toDescription() {
    return new Description(description);
  }

  Price toPrice() {
    return new Price(price);
  }

  PaymentDate toPaymentDate() {
    return new PaymentDate(LocalDate.parse(paymentDate));
  }

  ExpenseAttributeIdentifier toExpenseAttributeIdentifier() {
    return new ExpenseAttributeIdentifier(attributeId);
  }
}
