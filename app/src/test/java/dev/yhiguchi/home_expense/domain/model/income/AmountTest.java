package dev.yhiguchi.home_expense.domain.model.income;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AmountTest {

  @Test
  void 正の値で生成できる() {
    Amount amount = new Amount(1);
    assertEquals(1, amount.value());
  }

  @Test
  void ゼロは例外をスローする() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Amount(0));
    assertEquals("金額は正の値でなければなりません", exception.getMessage());
  }

  @Test
  void 負の値は例外をスローする() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Amount(-1));
    assertEquals("金額は正の値でなければなりません", exception.getMessage());
  }
}
