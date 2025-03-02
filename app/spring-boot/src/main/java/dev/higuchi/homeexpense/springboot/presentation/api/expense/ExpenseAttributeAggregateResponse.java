package dev.higuchi.homeexpense.springboot.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.higuchi.homeexpense.query.model.expense.ExpenseAttributeAggregate;

record ExpenseAttributeAggregateResponse(
    @JsonProperty("attribute_id") String attributeId,
    @JsonProperty("attribute_name") String attributeName,
    @JsonProperty("total_amount") Integer totalAmount) {

  static ExpenseAttributeAggregateResponse from(ExpenseAttributeAggregate aggregate) {
    return new ExpenseAttributeAggregateResponse(
        aggregate.expenseAttributeIdentifier().value(),
        aggregate.expenseAttributeName().value(),
        aggregate.totalAmount());
  }
}
