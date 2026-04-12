package dev.yhiguchi.home_expense.domain.model.income;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeName;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class IncomeUpdaterTest {

  IncomeAttribute attribute =
      new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));

  Income existing =
      new Income(
          new IncomeIdentifier("inc-1"),
          new Description("4月給与"),
          new Amount(300000),
          new ReceiveDate(LocalDate.of(2026, 4, 25)),
          attribute,
          1L);

  @Test
  void 収入を更新する() {
    List<Income> updated = new ArrayList<>();

    IncomeUpdater updater = new IncomeUpdater(id -> existing, attrId -> attribute, updated::add);

    updater.update(
        new IncomeIdentifier("inc-1"),
        new Description("4月給与（修正）"),
        new Amount(350000),
        new ReceiveDate(LocalDate.of(2026, 4, 25)),
        new IncomeAttributeIdentifier("attr-1"));

    assertEquals(1, updated.size());
    assertEquals("4月給与（修正）", updated.getFirst().description().value());
    assertEquals(350000, updated.getFirst().amount().value());
  }

  @Test
  void 変更がない場合は更新しない() {
    List<Income> updated = new ArrayList<>();

    IncomeUpdater updater = new IncomeUpdater(id -> existing, attrId -> attribute, updated::add);

    updater.update(
        new IncomeIdentifier("inc-1"),
        new Description("4月給与"),
        new Amount(300000),
        new ReceiveDate(LocalDate.of(2026, 4, 25)),
        new IncomeAttributeIdentifier("attr-1"));

    assertTrue(updated.isEmpty());
  }
}
