package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.ExpenseAttributeAggregate;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
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
