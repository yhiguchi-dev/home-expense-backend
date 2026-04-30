package dev.yhiguchi.home_expense.query.income.attribute;

import dev.yhiguchi.home_expense.query.Pagination;

public class IncomeAttributeSearchCriteria {
  Pagination pagination;

  public IncomeAttributeSearchCriteria(Pagination pagination) {
    this.pagination = pagination;
  }

  public int offset() {
    return pagination.offset();
  }

  public int perPage() {
    return pagination.perPage();
  }
}
