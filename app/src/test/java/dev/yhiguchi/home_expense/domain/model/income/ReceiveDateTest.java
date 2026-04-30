package dev.yhiguchi.home_expense.domain.model.income;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ReceiveDateTest {

  @Test
  void nullは例外をスローする() {
    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> new ReceiveDate(null));
    assertEquals("受取日は必須です", exception.getMessage());
  }
}
