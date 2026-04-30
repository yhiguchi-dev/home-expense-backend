package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResult;
import java.util.List;

public record ExpenseGetListResponse(@JsonProperty("expenses") List<ExpenseGetResponse> list) {

  public static ExpenseGetListResponse from(ExpenseSearchResult searchResult, int page) {
    if (page > searchResult.totalCount()) {
      return new ExpenseGetListResponse(List.of());
    }
    return new ExpenseGetListResponse(
        searchResult.list().stream().map(ExpenseGetResponse::from).toList());
  }
}
