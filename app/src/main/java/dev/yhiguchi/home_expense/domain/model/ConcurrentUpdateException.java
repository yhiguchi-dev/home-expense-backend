package dev.yhiguchi.home_expense.domain.model;

/** 並行更新例外 */
public class ConcurrentUpdateException extends RuntimeException {
  public ConcurrentUpdateException() {
    super();
  }
}
