package dev.yhiguchi.home_expense.domain.model.expense;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PriceTest {

  @Test
  void 正の値で生成できる() {
    Price price = new Price(1);
    assertEquals(1, price.value());
  }

  @Test
  void ゼロは例外をスローする() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Price(0));
    assertEquals("金額は正の値でなければなりません", exception.getMessage());
  }

  @Test
  void 負の値は例外をスローする() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Price(-1));
    assertEquals("金額は正の値でなければなりません", exception.getMessage());
  }
}
