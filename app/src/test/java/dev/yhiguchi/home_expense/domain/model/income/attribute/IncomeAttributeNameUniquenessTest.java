package dev.yhiguchi.home_expense.domain.model.income.attribute;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IncomeAttributeNameUniquenessTest {

  @Test
  void 同名が存在しない場合は成功する() {
    IncomeAttributeNameUniqueness sut = new IncomeAttributeNameUniqueness(name -> false);

    assertDoesNotThrow(() -> sut.assertUnique(new IncomeAttributeName("給与")));
  }

  @Test
  void 同名が存在する場合は例外をスローする() {
    IncomeAttributeNameUniqueness sut = new IncomeAttributeNameUniqueness(name -> true);

    assertThrows(
        IncomeAttributeAlreadyExistsException.class,
        () -> sut.assertUnique(new IncomeAttributeName("給与")));
  }
}
