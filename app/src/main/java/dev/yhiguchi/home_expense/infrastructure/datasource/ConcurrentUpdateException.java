package dev.yhiguchi.home_expense.infrastructure.datasource;

/** 並行更新例外 */
public class ConcurrentUpdateException extends RuntimeException {
  public ConcurrentUpdateException() {
    super();
  }
}
