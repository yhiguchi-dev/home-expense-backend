package dev.yhiguchi.home_expense.domain.model.expense;

import java.util.Arrays;

/** 経費分類 */
public enum ExpenseCategory {
  固定費,
  変動費;

  public boolean isFixed() {
    return this == 固定費;
  }

  public boolean isVariable() {
    return this == 変動費;
  }

  public static boolean has(String name) {
    return Arrays.stream(values()).anyMatch(e -> e.name().equals(name));
  }

  public static ExpenseCategory of(String value) {
    return Arrays.stream(values()).filter(e -> e.name().equals(value)).findFirst().orElseThrow();
  }
}
