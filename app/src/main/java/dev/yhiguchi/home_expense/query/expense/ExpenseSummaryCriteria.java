package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.query.Pagination;

public class ExpenseSummaryCriteria {
  Pagination pagination;
  SortOrder sortOrder;

  public ExpenseSummaryCriteria(Pagination pagination, SortOrder sortOrder) {
    this.pagination = pagination;
    this.sortOrder = sortOrder;
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

  public SortOrder sortOrder() {
    return sortOrder;
  }

  int getPerPage() {
    return perPage();
  }

  int getOffset() {
    return offset();
  }
}
