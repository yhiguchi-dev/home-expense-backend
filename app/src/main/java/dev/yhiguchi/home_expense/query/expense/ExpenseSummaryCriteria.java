package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.query.Pagination;
import java.time.YearMonth;

public class ExpenseSummaryCriteria {
  Pagination pagination;
  Integer year;
  Integer month;
  ExpenseCategory expenseCategory;

  public ExpenseSummaryCriteria(
      Pagination pagination, Integer year, Integer month, ExpenseCategory expenseCategory) {
    this.pagination = pagination;
    this.year = year;
    this.month = month;
    this.expenseCategory = expenseCategory;
  }

  public ExpenseSummaryCriteria(Pagination pagination, Integer year, Integer month) {
    this(pagination, year, month, null);
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
