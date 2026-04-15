package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResult;
import java.util.List;

public record ExpenseGetListResponse(@JsonProperty("expenses") List<ExpenseGetResponse> list) {

  ExpenseGetListResponse(ExpenseSearchResult searchResult) {
    this(searchResult.list().stream().map(ExpenseGetResponse::from).toList());
  }

  ExpenseGetListResponse() {
    this(List.of());
  }
}
