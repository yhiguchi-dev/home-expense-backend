package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExpenseAttributeDeletionPolicyTest {

  ExpenseAttribute attribute =
      new ExpenseAttribute(
          new ExpenseAttributeIdentifier("attr-1"),
          new ExpenseAttributeName("食費"),
          ExpenseCategory.変動費);

  @Test
  void 経費が存在しない場合は削除可能() {
    ExpenseAttributeDeletionPolicy sut =
        new ExpenseAttributeDeletionPolicy(a -> new Expenses());

    assertDoesNotThrow(() -> sut.assertDeletable(attribute));
  }

  @Test
  void 経費が存在する場合は例外をスローする() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attribute,
            1L);
    ExpenseAttributeDeletionPolicy sut =
        new ExpenseAttributeDeletionPolicy(a -> new Expenses(List.of(expense)));

    assertThrows(
        ExpenseAttributeConstraintException.class, () -> sut.assertDeletable(attribute));
  }
}
