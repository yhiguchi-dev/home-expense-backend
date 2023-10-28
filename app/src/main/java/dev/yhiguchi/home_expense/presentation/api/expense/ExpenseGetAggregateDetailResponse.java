package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.ExpenseAggregateDetail;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
class ExpenseGetAggregateDetailResponse {

  @JsonProperty("total_amount")
  int totalAmount;

  @JsonProperty("expenses")
  List<ExpenseGetResponse> list;

  ExpenseGetAggregateDetailResponse(ExpenseAggregateDetail detail) {
    this.totalAmount = detail.totalAmount();
    this.list = detail.list().stream().map(ExpenseGetResponse::from).toList();
  }
}
