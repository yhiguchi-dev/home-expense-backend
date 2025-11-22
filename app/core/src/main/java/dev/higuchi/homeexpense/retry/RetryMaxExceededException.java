package dev.higuchi.homeexpense.retry;

public class RetryMaxExceededException extends RuntimeException {
  public RetryMaxExceededException(int retry, Throwable cause) {
    super(String.format("retry max exceeded (%d)", retry), cause);
  }
}
