package dev.yhiguchi.home_expense.query.income.attribute;

import dev.yhiguchi.home_expense.query.Pagination;

public class IncomeAttributeSummaryCriteria {
  Pagination pagination;

  public IncomeAttributeSummaryCriteria(Pagination pagination) {
    this.pagination = pagination;
  }

  public int offset() {
    return pagination.offset();
  }

  public int perPage() {
    return pagination.perPage();
  }

  public int page() {
    return pagination.currentPage();
  }

  int getPerPage() {
    return perPage();
  }

  int getOffset() {
    return offset();
  }
}
