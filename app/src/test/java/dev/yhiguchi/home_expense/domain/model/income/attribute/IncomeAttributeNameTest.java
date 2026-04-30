package dev.yhiguchi.home_expense.domain.model.income.attribute;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IncomeAttributeNameTest {

  @Test
  void nullは例外をスローする() {
    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> new IncomeAttributeName(null));
    assertEquals("収入属性名は必須です", exception.getMessage());
  }
}
