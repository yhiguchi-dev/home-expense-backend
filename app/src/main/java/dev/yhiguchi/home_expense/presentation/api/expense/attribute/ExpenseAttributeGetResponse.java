package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummary;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
class ExpenseAttributeGetResponse {
  @JsonProperty("total_number")
  Integer totalNumber;

  @JsonProperty("page")
  Integer page;

  @JsonProperty("per_page")
  Integer perPage;

  @JsonProperty("expense_attributes")
  List<ExpenseAttributeResponse> list;

  ExpenseAttributeGetResponse(ExpenseAttributeCriteria criteria, ExpenseAttributeSummary summary) {
    this.totalNumber = summary.totalNumber();
    this.page = criteria.page();
    this.perPage = criteria.perPage();
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
