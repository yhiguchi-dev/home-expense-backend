package dev.yhiguchi.home_expense.domain.model.income;

/** 収入が見つからない */
public class IncomeNotFoundException extends RuntimeException {
  public IncomeNotFoundException() {
    super();
  }
}
