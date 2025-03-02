package dev.higuchi.homeexpense.command.model.expense;

/** 経費が見つからない */
public class ExpenseNotFoundException extends RuntimeException {
  public ExpenseNotFoundException() {
    super();
  }
}
