package dev.higuchi.homeexpense.retry;

import java.util.function.Function;

public sealed interface BackoffStrategy
    permits ConstantBackoffStrategy, ExponentialBackoffStrategy {
  Function<Integer, Long> backoff(Retry retry);
}
