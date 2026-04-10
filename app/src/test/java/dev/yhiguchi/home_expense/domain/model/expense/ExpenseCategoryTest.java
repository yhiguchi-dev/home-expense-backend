package dev.yhiguchi.home_expense.domain.model.expense;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ExpenseCategoryTest {

  @Test
  void of_固定費() {
    assertEquals(ExpenseCategory.固定費, ExpenseCategory.of("固定費"));
  }

  @Test
  void of_変動費() {
    assertEquals(ExpenseCategory.変動費, ExpenseCategory.of("変動費"));
  }

  @Test
  void of_存在しない値は例外() {
    assertThrows(Exception.class, () -> ExpenseCategory.of("不明"));
  }

  @Test
  void has_存在する値() {
    assertTrue(ExpenseCategory.has("固定費"));
    assertTrue(ExpenseCategory.has("変動費"));
  }

  @Test
  void has_存在しない値() {
    assertFalse(ExpenseCategory.has("不明"));
  }
}
