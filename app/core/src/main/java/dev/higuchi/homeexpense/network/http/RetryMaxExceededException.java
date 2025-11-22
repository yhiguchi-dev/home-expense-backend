package dev.higuchi.homeexpense.network.http;

public class RetryMaxExceededException extends RuntimeException {
  public RetryMaxExceededException(int retry, Throwable cause) {
    super(String.format("retry max exceeded (%d)", retry), cause);
  }
}
