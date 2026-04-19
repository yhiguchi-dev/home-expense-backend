package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import org.junit.jupiter.api.Test;

class ExpenseAttributeDeletionPolicyTest {

  ExpenseAttribute attribute =
      new ExpenseAttribute(
          new ExpenseAttributeIdentifier("attr-1"),
          new ExpenseAttributeName("食費"),
          ExpenseCategory.変動費,
          1L);

  @Test
  void 経費が存在しない場合は削除可能() {
    ExpenseAttributeDeletionPolicy sut = new ExpenseAttributeDeletionPolicy(a -> false);

    assertDoesNotThrow(() -> sut.assertDeletable(attribute));
  }

  @Test
  void 経費が存在する場合は例外をスローする() {
    ExpenseAttributeDeletionPolicy sut = new ExpenseAttributeDeletionPolicy(a -> true);

    assertThrows(ExpenseAttributeConstraintException.class, () -> sut.assertDeletable(attribute));
  }
}
