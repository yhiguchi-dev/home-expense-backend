package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSearchResult;
import java.util.List;

public record ExpenseAttributeGetListResponse(
    @JsonProperty("expense_attributes") List<ExpenseAttributeGetResponse> list) {

  ExpenseAttributeGetListResponse(ExpenseAttributeSearchResult searchResult) {
    this(searchResult.list().stream().map(ExpenseAttributeGetResponse::from).toList());
  }

  ExpenseAttributeGetListResponse() {
    this(List.of());
  }
}
