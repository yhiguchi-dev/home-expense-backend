package dev.yhiguchi.home_expense.query.expense;

import java.time.LocalDate;
import java.time.YearMonth;

public class ExpenseAggregateCriteria {

  int year;

  int month;

  public ExpenseAggregateCriteria(int year, int month) {
    this.year = year;
    this.month = month;
  }

  public LocalDate dateFrom() {
    return YearMonth.of(year, month).atDay(1);
  }

  public LocalDate dateTo() {
    return YearMonth.of(year, month).plusMonths(1).atDay(1);
  }
}
