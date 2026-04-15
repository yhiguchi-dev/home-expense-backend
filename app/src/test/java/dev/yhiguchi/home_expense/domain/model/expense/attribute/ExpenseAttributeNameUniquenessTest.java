package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ExpenseAttributeNameUniquenessTest {

  @Test
  void 同名が存在しない場合は成功する() {
    ExpenseAttributeNameUniqueness sut = new ExpenseAttributeNameUniqueness(name -> false);

    assertDoesNotThrow(() -> sut.assertUnique(new ExpenseAttributeName("食費")));
  }

  @Test
  void 同名が存在する場合は例外をスローする() {
    ExpenseAttributeNameUniqueness sut = new ExpenseAttributeNameUniqueness(name -> true);

    assertThrows(
        ExpenseAttributeAlreadyExistsException.class,
        () -> sut.assertUnique(new ExpenseAttributeName("食費")));
  }
}
