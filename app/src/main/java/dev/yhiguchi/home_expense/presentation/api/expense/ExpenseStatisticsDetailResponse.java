package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.ExpenseStatisticsDetail;
import java.util.List;

class ExpenseStatisticsDetailResponse {

  @JsonProperty("total_amount")
  long totalAmount;

  @JsonProperty("attribute_statistics")
  List<ExpenseAttributeStatisticsResponse> list;

  ExpenseStatisticsDetailResponse(ExpenseStatisticsDetail detail) {
    this.totalAmount = detail.totalAmount();
    this.list = detail.list().stream().map(ExpenseAttributeStatisticsResponse::from).toList();
  }
}
