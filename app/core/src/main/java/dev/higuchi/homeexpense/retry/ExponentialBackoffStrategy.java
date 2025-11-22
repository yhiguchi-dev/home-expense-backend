package dev.higuchi.homeexpense.retry;

import java.util.function.Function;

public record ExponentialBackoffStrategy() implements BackoffStrategy {

  @Override
  public Function<Integer, Long> backoff(Retry retry) {
    return attempt -> (long) Math.pow(attempt, 2) * retry.delay();
  }
}
