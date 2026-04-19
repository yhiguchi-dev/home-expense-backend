package dev.yhiguchi.home_expense.domain.model.income.attribute;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IncomeAttributeDeletionPolicyTest {

  IncomeAttribute attribute =
      new IncomeAttribute(
          new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"), 1L);

  @Test
  void 収入が存在しない場合は削除可能() {
    IncomeAttributeDeletionPolicy sut = new IncomeAttributeDeletionPolicy(a -> false);

    assertDoesNotThrow(() -> sut.assertDeletable(attribute));
  }

  @Test
  void 収入が存在する場合は例外をスローする() {
    IncomeAttributeDeletionPolicy sut = new IncomeAttributeDeletionPolicy(a -> true);

    assertThrows(IncomeAttributeConstraintException.class, () -> sut.assertDeletable(attribute));
  }
}
