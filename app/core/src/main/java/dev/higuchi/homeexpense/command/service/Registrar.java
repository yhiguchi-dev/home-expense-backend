package dev.higuchi.homeexpense.command.service;

@FunctionalInterface
public interface Registrar<TYPE, RETURN> {
  RETURN register(TYPE type);
}
