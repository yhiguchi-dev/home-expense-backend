package dev.higuchi.homeexpense.springboot.presentation.api.income;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.higuchi.homeexpense.command.model.income.Income;

record IncomeGetResponse(
    @JsonProperty("id") String id,
    @JsonProperty("description") String description,
    @JsonProperty("amount") Integer amount,
    @JsonProperty("receive_date") String receiveDate,
    @JsonProperty("income_attribute") IncomeAttributeResponse incomeAttributeResponse) {

  static IncomeGetResponse from(Income income) {
    IncomeAttributeResponse expenseAttributeResponse =
        IncomeAttributeResponse.from(income.incomeAttribute());
    return new IncomeGetResponse(
        income.incomeIdentifier().value(),
        income.description().value(),
        income.amount().value(),
        income.receiveDate().value(),
        expenseAttributeResponse);
  }
}
