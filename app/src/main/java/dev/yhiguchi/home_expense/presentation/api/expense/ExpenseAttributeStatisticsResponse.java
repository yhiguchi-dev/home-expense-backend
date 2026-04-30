package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.ExpenseAttributeStatistics;

public record ExpenseAttributeStatisticsResponse(
    @JsonProperty("attribute_id") String attributeId,
    @JsonProperty("attribute_name") String attributeName,
    @JsonProperty("total_amount") Long totalAmount) {

  static ExpenseAttributeStatisticsResponse from(ExpenseAttributeStatistics statistics) {
    return new ExpenseAttributeStatisticsResponse(
        statistics.attributeId(), statistics.attributeName(), statistics.totalAmount());
  }
}
