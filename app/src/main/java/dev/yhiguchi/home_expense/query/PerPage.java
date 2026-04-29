package dev.yhiguchi.home_expense.query;

public record PerPage(int value) {

  public static final int MIN_VALUE = 1;
  public static final int MAX_VALUE = 100;
}
