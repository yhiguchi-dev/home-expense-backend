package dev.yhiguchi.home_expense.domain.model.expense;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PaymentDateTest {

  @Test
  void nullは例外をスローする() {
    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> new PaymentDate(null));
    assertEquals("支払日は必須です", exception.getMessage());
  }
}
