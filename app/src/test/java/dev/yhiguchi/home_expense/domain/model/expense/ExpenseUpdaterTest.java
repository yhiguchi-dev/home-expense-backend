package dev.yhiguchi.home_expense.domain.model.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExpenseUpdaterTest {

  ExpenseAttribute attribute =
      new ExpenseAttribute(
          new ExpenseAttributeIdentifier("attr-1"),
          new ExpenseAttributeName("食費"),
          ExpenseCategory.変動費);

  Expense existing =
      new Expense(
          new ExpenseIdentifier("exp-1"),
          new Description("ランチ"),
          new Price(1000),
          new PaymentDate(LocalDate.of(2026, 4, 1)),
          attribute,
          1L);

  @Test
  void 経費を更新する() {
    List<Expense> updated = new ArrayList<>();

    ExpenseUpdater updater = new ExpenseUpdater(id -> existing, attrId -> attribute, updated::add);

    updater.update(
        new ExpenseIdentifier("exp-1"),
        new Description("ディナー"),
        new Price(2000),
        new PaymentDate(LocalDate.of(2026, 4, 2)),
        new ExpenseAttributeIdentifier("attr-1"));

    assertEquals(1, updated.size());
    assertEquals("ディナー", updated.getFirst().description().value());
    assertEquals(2000, updated.getFirst().price().value());
  }

  @Test
  void 変更がない場合は更新しない() {
    List<Expense> updated = new ArrayList<>();

    ExpenseUpdater updater = new ExpenseUpdater(id -> existing, attrId -> attribute, updated::add);

    updater.update(
        new ExpenseIdentifier("exp-1"),
        new Description("ランチ"),
        new Price(1000),
        new PaymentDate(LocalDate.of(2026, 4, 1)),
        new ExpenseAttributeIdentifier("attr-1"));

    assertTrue(updated.isEmpty());
  }
}
