package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummary;
import java.util.List;

class ExpenseAttributeGetSummaryResponse {

  @JsonProperty("expense_attributes")
  List<ExpenseAttributeGetResponse> list;

  ExpenseAttributeGetSummaryResponse(ExpenseAttributeSummary summary) {
    this.list = summary.list().stream().map(ExpenseAttributeGetResponse::from).toList();
  }

  ExpenseAttributeGetSummaryResponse() {
    this.list = List.of();
  }
}
