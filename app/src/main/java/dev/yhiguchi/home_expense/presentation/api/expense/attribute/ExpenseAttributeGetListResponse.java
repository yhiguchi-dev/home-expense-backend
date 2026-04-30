package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSearchResult;
import java.util.List;

public record ExpenseAttributeGetListResponse(
    @JsonProperty("expense_attributes") List<ExpenseAttributeGetResponse> list) {

  public static ExpenseAttributeGetListResponse from(
      ExpenseAttributeSearchResult searchResult, int page) {
    if (page > searchResult.totalCount()) {
      return new ExpenseAttributeGetListResponse(List.of());
    }
    return new ExpenseAttributeGetListResponse(
        searchResult.list().stream().map(ExpenseAttributeGetResponse::from).toList());
  }
}
