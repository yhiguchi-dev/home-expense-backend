package dev.yhiguchi.home_expense.presentation.api.income;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.income.IncomeSearchResult;
import java.util.List;

public record IncomeGetListResponse(@JsonProperty("incomes") List<IncomeGetResponse> list) {

  IncomeGetListResponse(IncomeSearchResult searchResult) {
    this(searchResult.list().stream().map(IncomeGetResponse::from).toList());
  }

  IncomeGetListResponse() {
    this(List.of());
  }
}
