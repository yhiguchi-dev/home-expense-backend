package dev.yhiguchi.home_expense.presentation.api.income;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.domain.model.income.Amount;
import dev.yhiguchi.home_expense.domain.model.income.Description;
import dev.yhiguchi.home_expense.domain.model.income.ReceiveDate;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import java.time.LocalDate;

public record IncomePutRequest(
    @JsonProperty("description") String description,
    @JsonProperty("amount") Integer amount,
    @JsonProperty("receive_date") String receiveDate,
    @JsonProperty("attribute_id") String attributeId) {

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
