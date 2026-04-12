package dev.yhiguchi.home_expense.domain.model;

/** 楽観ロック例外 */
public class OptimisticLockException extends RuntimeException {
  public OptimisticLockException() {
    super();
  }
}
