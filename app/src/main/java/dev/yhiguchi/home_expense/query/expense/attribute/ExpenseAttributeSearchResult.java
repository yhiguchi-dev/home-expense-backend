package dev.yhiguchi.home_expense.query.expense.attribute;

import java.util.List;

public record ExpenseAttributeSearchResult(
    Integer totalCount, List<ExpenseAttributeDetail> list) {

  public static ExpenseAttributeSearchResult empty() {
    return new ExpenseAttributeSearchResult(0, List.of());
  }
}
