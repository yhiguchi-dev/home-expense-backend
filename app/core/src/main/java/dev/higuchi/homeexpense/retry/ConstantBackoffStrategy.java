package dev.higuchi.homeexpense.retry;

import java.util.function.Function;

public record ConstantBackoffStrategy() implements BackoffStrategy {
  @Override
  public Function<Integer, Long> backoff(Retry retry) {
    return attempt -> retry.delay();
  }
}
