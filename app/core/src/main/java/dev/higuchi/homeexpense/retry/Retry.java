package dev.higuchi.homeexpense.retry;

public record Retry(int maxAttempts, int timeout, long delay, BackoffStrategy strategy) {

  Long delayWithBackoff(int attempt) {
    return strategy.backoff(this).apply(attempt);
  }
}
