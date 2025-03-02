package dev.higuchi.homeexpense.quarkus.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.higuchi.homeexpense.command.model.expense.Description;
import dev.higuchi.homeexpense.command.model.expense.PaymentDate;
import dev.higuchi.homeexpense.command.model.expense.Price;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import java.time.LocalDate;

public record ExpensePutRequest(
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
