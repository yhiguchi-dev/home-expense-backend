package dev.yhiguchi.home_expense.domain.model;

/** 永続化された集約とその版番号のペア */
public record Revision<T>(T entity, long version) {}
