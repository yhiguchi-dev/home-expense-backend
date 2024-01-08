package dev.yhiguchi.home_expense.query.income;

import dev.yhiguchi.home_expense.query.Pagination;

public class IncomeSummaryCriteria {
  Pagination pagination;
  Integer year;

  public IncomeSummaryCriteria(Pagination pagination, Integer year) {
    this.pagination = pagination;
    this.year = year;
  }

  public int offset() {
    return pagination.offset();
  }

  public int perPage() {
    return pagination.perPage();
  }

  public Pagination pagination() {
    return pagination;
  }

  int getPerPage() {
    return perPage();
  }

  int getOffset() {
    return offset();
  }
}
