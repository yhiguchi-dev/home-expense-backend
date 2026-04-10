package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExpenseAttributeUpdaterTest {

  ExpenseAttribute existing =
      new ExpenseAttribute(
          new ExpenseAttributeIdentifier("attr-1"),
          new ExpenseAttributeName("食費"),
          ExpenseCategory.変動費);

  @Test
  void 経費属性を更新する() {
    List<ExpenseAttribute> updated = new ArrayList<>();

    ExpenseAttributeUpdater updater = new ExpenseAttributeUpdater(id -> existing, updated::add);

    updater.update(
        new ExpenseAttributeIdentifier("attr-1"),
        new ExpenseAttributeName("外食費"),
        ExpenseCategory.変動費);

    assertEquals(1, updated.size());
    assertEquals("外食費", updated.getFirst().expenseAttributeName().value());
  }

  @Test
  void 変更がない場合は更新しない() {
    List<ExpenseAttribute> updated = new ArrayList<>();

    ExpenseAttributeUpdater updater = new ExpenseAttributeUpdater(id -> existing, updated::add);

    updater.update(
        new ExpenseAttributeIdentifier("attr-1"),
        new ExpenseAttributeName("食費"),
        ExpenseCategory.変動費);

    assertTrue(updated.isEmpty());
  }
}
