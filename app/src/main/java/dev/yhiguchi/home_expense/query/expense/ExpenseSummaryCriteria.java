package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.query.Pagination;
import java.time.YearMonth;

public class ExpenseSummaryCriteria {
  Pagination pagination;
  Integer year;
  Integer month;
  SortOrder sortOrder;

  public ExpenseSummaryCriteria(
      Pagination pagination, Integer year, Integer month, SortOrder sortOrder) {
    this.pagination = pagination;
    this.year = year;
    this.month = month;
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

  String yearMonth() {
    return YearMonth.of(year, month).toString();
  }

  int getPerPage() {
    return perPage();
  }

  int getOffset() {
    return offset();
  }

  String getYearMonth() {
    return yearMonth();
  }
}
