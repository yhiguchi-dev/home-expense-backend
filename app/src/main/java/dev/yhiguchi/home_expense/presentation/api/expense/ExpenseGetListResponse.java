package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResult;
import java.util.List;

class ExpenseGetListResponse {
  @JsonProperty("expenses")
  List<ExpenseGetResponse> list;

  ExpenseGetListResponse(ExpenseSearchResult searchResult) {
    this.list = searchResult.list().stream().map(ExpenseGetResponse::from).toList();
  }

  ExpenseGetListResponse() {
    this.list = List.of();
  }
}
