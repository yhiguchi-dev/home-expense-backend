package dev.yhiguchi.home_expense.presentation.api.income.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchResult;
import java.util.List;

public record IncomeAttributeGetListResponse(
    @JsonProperty("income_attributes") List<IncomeAttributeGetResponse> list) {

  public static IncomeAttributeGetListResponse from(
      IncomeAttributeSearchResult searchResult, int page) {
    if (page > searchResult.totalCount()) {
      return new IncomeAttributeGetListResponse(List.of());
    }
    return new IncomeAttributeGetListResponse(
        searchResult.list().stream().map(IncomeAttributeGetResponse::from).toList());
  }
}
