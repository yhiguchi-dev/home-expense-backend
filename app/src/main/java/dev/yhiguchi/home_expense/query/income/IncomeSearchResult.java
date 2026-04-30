package dev.yhiguchi.home_expense.query.income;

import java.util.List;

public record IncomeSearchResult(Integer totalCount, List<IncomeDetail> list) {

  public static IncomeSearchResult empty() {
    return new IncomeSearchResult(0, List.of());
  }
}
