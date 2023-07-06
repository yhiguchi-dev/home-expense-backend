package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.ExpenseCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummary;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
class ExpenseGetResponse {
  @JsonProperty("total_number")
  Integer totalNumber;

  @JsonProperty("page")
  Integer page;

  @JsonProperty("per_page")
  Integer perPage;

  @JsonProperty("expenses")
  List<ExpenseResponse> list;

  ExpenseGetResponse(ExpenseCriteria criteria, ExpenseSummary summary) {
    this.totalNumber = summary.totalNumber();
    this.page = criteria.page();
    this.perPage = criteria.perPage();
    this.list =
        summary.list().stream()
            .map(
                e -> {
                  ExpenseAttributeResponse expenseAttributeResponse =
                      e.hasAttribute()
                          ? new ExpenseAttributeResponse(
                              e.expenseAttribute().expenseAttributeIdentifier().value(),
                              e.expenseAttribute().expenseAttributeName().value(),
                              e.expenseAttribute().expenseCategory().name())
                          : null;
                  return new ExpenseResponse(
                      e.expenseIdentifier().value(),
                      e.description().value(),
                      e.price().value(),
                      e.paymentDate().value(),
                      expenseAttributeResponse);
                })
            .toList();
  }
}
