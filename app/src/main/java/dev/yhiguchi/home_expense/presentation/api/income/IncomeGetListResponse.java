package dev.yhiguchi.home_expense.presentation.api.income;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.income.IncomeSearchResult;
import java.util.List;

public record IncomeGetListResponse(@JsonProperty("incomes") List<IncomeGetResponse> list) {

  public static IncomeGetListResponse from(IncomeSearchResult searchResult, int page) {
    if (page > searchResult.totalCount()) {
      return new IncomeGetListResponse(List.of());
    }
    return new IncomeGetListResponse(
        searchResult.list().stream().map(IncomeGetResponse::from).toList());
  }
}
