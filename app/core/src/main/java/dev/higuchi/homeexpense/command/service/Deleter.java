package dev.higuchi.homeexpense.command.service;

@FunctionalInterface
public interface Deleter<KEY> {
  void delete(KEY key);
}
