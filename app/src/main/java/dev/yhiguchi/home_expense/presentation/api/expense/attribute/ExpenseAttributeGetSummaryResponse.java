package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummary;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
class ExpenseAttributeGetSummaryResponse {

  @JsonProperty("expense_attributes")
  List<ExpenseAttributeResponse> list;

  ExpenseAttributeGetSummaryResponse(ExpenseAttributeSummary summary) {
    this.list =
        summary.list().stream()
            .map(
                e ->
                    new ExpenseAttributeResponse(
                        e.expenseAttributeIdentifier().value(),
                        e.expenseAttributeName().value(),
                        e.expenseCategory().name()))
            .toList();
  }
}
