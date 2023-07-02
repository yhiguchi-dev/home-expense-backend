package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.query.Pagination;

public class ExpenseCriteria {
  Pagination pagination;

  public ExpenseCriteria(Pagination pagination) {
    this.pagination = pagination;
  }

  public int offset() {
    return pagination.offset();
  }

  public int perPage() {
    return pagination.perPage();
  }

  public int page() {
    return pagination.page();
  }

  public int getPerPage() {
    return perPage();
  }

  public int getOffset() {
    return offset();
  }
}
