package dev.higuchi.homeexpense.command.service;

import java.util.Optional;

@FunctionalInterface
public interface Fetcher<KEY, RETURN> {
  Optional<RETURN> fetch(KEY key);

  default RETURN getOrThrow(KEY key, RuntimeException ex) {
    return fetch(key).orElseThrow(() -> ex);
  }

  default RETURN findBy(KEY key, RETURN defaultValue) {
    return fetch(key).orElse(defaultValue);
  }
}
