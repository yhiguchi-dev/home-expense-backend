package dev.higuchi.homeexpense.springboot.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.higuchi.homeexpense.query.model.expense.ExpenseAggregateDetail;
import java.util.List;

class ExpenseAggregateDetailResponse {

  @JsonProperty("total_amount")
  int totalAmount;

  @JsonProperty("attribute_aggregates")
  List<ExpenseAttributeAggregateResponse> list;

  ExpenseAggregateDetailResponse(ExpenseAggregateDetail detail) {
    this.totalAmount = detail.totalAmount();
    this.list = detail.list().stream().map(ExpenseAttributeAggregateResponse::from).toList();
  }
}
