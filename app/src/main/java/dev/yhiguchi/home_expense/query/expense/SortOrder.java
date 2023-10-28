package dev.yhiguchi.home_expense.query.expense;

import java.util.Arrays;

public enum SortOrder {
  asc,
  desc;

  public static boolean has(String name) {
    return Arrays.stream(values()).anyMatch(e -> e.name().equals(name));
  }

  public static SortOrder of(String value) {
    return Arrays.stream(values()).filter(e -> e.name().equals(value)).findFirst().orElseThrow();
  }
}
