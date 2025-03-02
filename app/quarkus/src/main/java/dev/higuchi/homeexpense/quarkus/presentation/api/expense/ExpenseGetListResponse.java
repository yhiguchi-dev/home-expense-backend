package dev.higuchi.homeexpense.quarkus.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummary;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
class ExpenseGetListResponse {
  @JsonProperty("expenses")
  List<ExpenseGetResponse> list;

  ExpenseGetListResponse(ExpenseSummary summary) {
    this.list = summary.list().stream().map(ExpenseGetResponse::from).toList();
  }

  ExpenseGetListResponse() {
    this.list = List.of();
  }
}
