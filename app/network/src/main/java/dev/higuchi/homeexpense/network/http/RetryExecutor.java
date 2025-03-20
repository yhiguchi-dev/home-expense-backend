package dev.higuchi.homeexpense.network.http;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public class RetryExecutor<TYPE> {

  Executor executor;
  int retries;
  int timeout;
  int delay;
  Function<Integer, Integer> backoff;

  RetryExecutor(Executor executor, int retries, int timeout, int delay) {
    this.executor = executor;
    this.retries = retries;
    this.timeout = timeout;
    this.delay = delay;
  }

  public CompletableFuture<TYPE> execute(
      Supplier<TYPE> supplier, Function<Throwable, Boolean> isRetry) {
    return CompletableFuture.supplyAsync(supplier, executor)
        .thenApply(CompletableFuture::completedFuture)
        .exceptionally(
            throwable -> {
              Throwable cause = throwable.getCause();
              if (isRetry.apply(cause)) {
                return retry(supplier, isRetry, cause, 0);
              }
              return CompletableFuture.failedFuture(cause);
            })
        .thenCompose(Function.identity());
  }

  CompletableFuture<TYPE> retry(
      Supplier<TYPE> supplier, Function<Throwable, Boolean> isRetry, Throwable cause, int retry) {
    if (retry >= retries) {
      return CompletableFuture.failedFuture(new RetryMaxExceededException(retry, cause));
    }
    var delayed =
        CompletableFuture.delayedExecutor(
            exponentialBackoffAndFullJitter(retry), TimeUnit.SECONDS, executor);
    return CompletableFuture.supplyAsync(supplier, delayed)
        .thenApply(CompletableFuture::completedFuture)
        .exceptionally(
            throwable -> {
              Throwable retryCause = throwable.getCause();
              if (isRetry.apply(retryCause)) {
                cause.addSuppressed(retryCause);
                return retry(supplier, isRetry, retryCause, retry + 1);
              }
              return CompletableFuture.failedFuture(retryCause);
            })
        .thenCompose(Function.identity());
  }

  int exponentialBackoffAndFullJitter(int retry) {
    int capped = Math.min(timeout, delay * 2 ^ retry);
    Random random = new Random();
    return random.ints(0, capped).findFirst().getAsInt();
  }
}
