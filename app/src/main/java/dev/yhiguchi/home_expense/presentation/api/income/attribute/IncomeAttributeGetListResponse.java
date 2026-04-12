package dev.yhiguchi.home_expense.presentation.api.income.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchResult;
import java.util.List;

class IncomeAttributeGetListResponse {

  @JsonProperty("income_attributes")
  List<IncomeAttributeGetResponse> list;

  IncomeAttributeGetListResponse(IncomeAttributeSearchResult searchResult) {
    this.list = searchResult.list().stream().map(IncomeAttributeGetResponse::from).toList();
  }

  IncomeAttributeGetListResponse() {
    this.list = List.of();
  }
}
