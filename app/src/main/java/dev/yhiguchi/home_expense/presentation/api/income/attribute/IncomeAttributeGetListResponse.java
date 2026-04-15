package dev.yhiguchi.home_expense.presentation.api.income.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchResult;
import java.util.List;

public record IncomeAttributeGetListResponse(
    @JsonProperty("income_attributes") List<IncomeAttributeGetResponse> list) {

  IncomeAttributeGetListResponse(IncomeAttributeSearchResult searchResult) {
    this(searchResult.list().stream().map(IncomeAttributeGetResponse::from).toList());
  }

  IncomeAttributeGetListResponse() {
    this(List.of());
  }
}
