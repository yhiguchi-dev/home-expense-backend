package dev.yhiguchi.home_expense.query.income.attribute;

import java.util.List;

public record IncomeAttributeSearchResult(Integer totalCount, List<IncomeAttributeDetail> list) {

  public static IncomeAttributeSearchResult empty() {
    return new IncomeAttributeSearchResult(0, List.of());
  }
}
