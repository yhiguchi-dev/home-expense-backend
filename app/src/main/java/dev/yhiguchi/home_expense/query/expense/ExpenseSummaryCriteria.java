package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.query.Pagination;
import java.time.YearMonth;
import java.util.Objects;

public class ExpenseSummaryCriteria {
  Pagination pagination;
  Integer year;
  Integer month;
  ExpenseCategory expenseCategory;
  ExpenseAttributeIdentifier expenseAttributeIdentifier;

  public ExpenseSummaryCriteria(
      Pagination pagination,
      Integer year,
      Integer month,
      ExpenseCategory expenseCategory,
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    this.pagination = pagination;
    this.year = year;
    this.month = month;
    this.expenseCategory = expenseCategory;
    this.expenseAttributeIdentifier = expenseAttributeIdentifier;
  }

  public ExpenseSummaryCriteria(
      Pagination pagination,
      Integer year,
      Integer month,
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    this.pagination = pagination;
    this.year = year;
    this.month = month;
    this.expenseAttributeIdentifier = expenseAttributeIdentifier;
  }

  public Pagination pagination() {
    return pagination;
  }

  String yearMonth() {
    return YearMonth.of(year, month).toString();
  }

  int getPerPage() {
    return pagination.perPage();
  }

  int getOffset() {
    return pagination.offset();
  }

  String getYearMonth() {
    return yearMonth();
  }

  public boolean hasYear() {
    return Objects.nonNull(year);
  }

  int getYear() {
    return year;
  }

  public boolean hasMonth() {
    return Objects.nonNull(month);
  }

  public int getMonth() {
    return month;
  }

  public boolean hasExpenseCategory() {
    return Objects.nonNull(expenseCategory);
  }

  ExpenseCategory getExpenseCategory() {
    return expenseCategory;
  }

  public boolean hasExpenseAttributeIdentifier() {
    return expenseAttributeIdentifier.exists();
  }

  String getExpenseAttributeIdentifier() {
    return expenseAttributeIdentifier.value();
  }
}
