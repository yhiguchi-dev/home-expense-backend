package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import dev.yhiguchi.home_expense.query.Pagination;
import java.time.YearMonth;

public class ExpenseSummaryCriteria {
  Pagination pagination;
  Integer year;
  Integer month;
  ExpenseCategory expenseCategory;
  ExpenseAttributeName expenseAttributeName;

  public ExpenseSummaryCriteria(
      Pagination pagination,
      Integer year,
      Integer month,
      ExpenseCategory expenseCategory,
      ExpenseAttributeName expenseAttributeName) {
    this.pagination = pagination;
    this.year = year;
    this.month = month;
    this.expenseCategory = expenseCategory;
    this.expenseAttributeName = expenseAttributeName;
  }

  public ExpenseSummaryCriteria(
      Pagination pagination,
      Integer year,
      Integer month,
      ExpenseAttributeName expenseAttributeName) {
    this.pagination = pagination;
    this.year = year;
    this.month = month;
    this.expenseAttributeName = expenseAttributeName;
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

  String getYear() {
    return year.toString();
  }

  public ExpenseCategory getExpenseCategory() {
    return expenseCategory;
  }

  public ExpenseAttributeName getExpenseAttributeName() {
    return expenseAttributeName;
  }
}
