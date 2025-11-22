package dev.higuchi.homeexpense.retry;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

class RetryExecutor<TYPE> {

  Executor executor;
  Retry retry;

  RetryExecutor(Executor executor, Retry retry) {
    this.executor = executor;
    this.retry = retry;
  }

  CompletableFuture<TYPE> execute(Supplier<TYPE> retryFn, Function<Throwable, Boolean> isRetry) {
    return CompletableFuture.supplyAsync(retryFn, executor)
        .thenApply(CompletableFuture::completedFuture)
        .exceptionally(
            throwable -> {
              Throwable cause = throwable.getCause();
              if (isRetry.apply(cause)) {
                return retry(retryFn, isRetry, cause, 1);
              }
              return CompletableFuture.failedFuture(cause);
            })
        .thenCompose(Function.identity());
  }

  CompletableFuture<TYPE> retry(
      Supplier<TYPE> retryFn, Function<Throwable, Boolean> isRetry, Throwable cause, int attempt) {
    if (attempt >= retry.maxAttempts()) {
      return CompletableFuture.failedFuture(new RetryMaxExceededException(attempt, cause));
    }
    Long delay = retry.delayWithBackoff(attempt);
    var delayed = CompletableFuture.delayedExecutor(delay, TimeUnit.SECONDS, executor);
    return CompletableFuture.supplyAsync(retryFn, delayed)
        .thenApply(CompletableFuture::completedFuture)
        .exceptionally(
            throwable -> {
              Throwable retryCause = throwable.getCause();
              retryCause.addSuppressed(cause);
              if (isRetry.apply(retryCause)) {
                return retry(retryFn, isRetry, retryCause, attempt + 1);
              }
              return CompletableFuture.failedFuture(retryCause);
            })
        .thenCompose(Function.identity());
  }
}
