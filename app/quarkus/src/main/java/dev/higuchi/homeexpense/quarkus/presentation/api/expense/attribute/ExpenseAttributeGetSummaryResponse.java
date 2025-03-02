package dev.higuchi.homeexpense.quarkus.presentation.api.expense.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummary;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
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
