package dev.yhiguchi.home_expense.query.expense;

import java.time.YearMonth;

public class ExpenseAggregateCriteria {

  int year;

  int month;

  public ExpenseAggregateCriteria(int year, int month) {
    this.year = year;
    this.month = month;
  }

  String getYearMonth() {
    return YearMonth.of(year, month).toString();
  }
}
