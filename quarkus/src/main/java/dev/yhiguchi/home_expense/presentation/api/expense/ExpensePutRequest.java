package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.domain.model.expense.Description;
import dev.yhiguchi.home_expense.domain.model.expense.PaymentDate;
import dev.yhiguchi.home_expense.domain.model.expense.Price;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import java.time.LocalDate;

record ExpensePutRequest(
    @JsonProperty("description") String description,
    @JsonProperty("price") Integer price,
    @JsonProperty("payment_date") String paymentDate,
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
