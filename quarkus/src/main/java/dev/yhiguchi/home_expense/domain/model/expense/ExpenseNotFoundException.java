package dev.yhiguchi.home_expense.domain.model.expense;

/** 経費が見つからない */
public class ExpenseNotFoundException extends RuntimeException {
  public ExpenseNotFoundException() {
    super();
  }
}
