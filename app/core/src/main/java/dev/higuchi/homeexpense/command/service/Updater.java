package dev.higuchi.homeexpense.command.service;

@FunctionalInterface
public interface Updater<TYPE, RETURN> {
  RETURN update(TYPE type);
}
