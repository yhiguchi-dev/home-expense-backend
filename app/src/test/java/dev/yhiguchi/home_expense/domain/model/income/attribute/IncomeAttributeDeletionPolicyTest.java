package dev.yhiguchi.home_expense.domain.model.income.attribute;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.income.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class IncomeAttributeDeletionPolicyTest {

  IncomeAttribute attribute =
      new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));

  @Test
  void 収入が存在しない場合は削除可能() {
    IncomeAttributeDeletionPolicy sut =
        new IncomeAttributeDeletionPolicy(a -> new Incomes());

    assertDoesNotThrow(() -> sut.assertDeletable(attribute));
  }

  @Test
  void 収入が存在する場合は例外をスローする() {
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute,
            1L);
    IncomeAttributeDeletionPolicy sut =
        new IncomeAttributeDeletionPolicy(a -> new Incomes(List.of(income)));

    assertThrows(
        IncomeAttributeConstraintException.class, () -> sut.assertDeletable(attribute));
  }
}
