package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.query.Pagination;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Objects;

public class ExpenseSearchCriteria {
  Pagination pagination;
  Integer year;
  Integer month;
  ExpenseCategory expenseCategory;
  ExpenseAttributeIdentifier expenseAttributeIdentifier;

  public ExpenseSearchCriteria(
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

  public ExpenseSearchCriteria(
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

  public boolean hasDateRange() {
    return Objects.nonNull(year);
  }

  public LocalDate dateFrom() {
    if (Objects.nonNull(month)) {
      return YearMonth.of(year, month).atDay(1);
    }
    return Year.of(year).atDay(1);
  }

  public LocalDate dateTo() {
    if (Objects.nonNull(month)) {
      return YearMonth.of(year, month).plusMonths(1).atDay(1);
    }
    return Year.of(year).plusYears(1).atDay(1);
  }

  public boolean hasExpenseCategory() {
    return Objects.nonNull(expenseCategory);
  }

  public ExpenseCategory getExpenseCategory() {
    return expenseCategory;
  }

  public boolean hasExpenseAttributeIdentifier() {
    return Objects.nonNull(expenseAttributeIdentifier);
  }

  public String getExpenseAttributeIdentifier() {
    return expenseAttributeIdentifier.value();
  }
}
