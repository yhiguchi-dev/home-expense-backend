package dev.yhiguchi.home_expense.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DescriptionTest {

  @Test
  void nullは例外をスローする() {
    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> new Description(null));
    assertEquals("説明は必須です", exception.getMessage());
  }
}
