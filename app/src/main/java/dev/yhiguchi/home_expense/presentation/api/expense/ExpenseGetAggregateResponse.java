package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.ExpenseAggregate;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
class ExpenseGetAggregateResponse {
  @JsonProperty("total_amount")
  Integer totalAmount;

  @JsonProperty("fixed_expense_detail")
  ExpenseGetAggregateDetailResponse fixedDetail;

  @JsonProperty("variable_expense_detail")
  ExpenseGetAggregateDetailResponse variableDetail;

  ExpenseGetAggregateResponse(ExpenseAggregate aggregate) {
    this.totalAmount = aggregate.totalAmount();
    this.fixedDetail = new ExpenseGetAggregateDetailResponse(aggregate.fixedDetail());
    this.variableDetail = new ExpenseGetAggregateDetailResponse(aggregate.variableDetail());
  }
}
