package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import org.junit.jupiter.api.Test;

class ExpenseAttributeTest {

  @Test
  void createでUUIDが生成されversion1のExpenseAttributeが作成される() {
    ExpenseAttribute attribute =
        ExpenseAttribute.create(new ExpenseAttributeName("食費"), ExpenseCategory.変動費);

    assertNotNull(attribute.expenseAttributeIdentifier().value());
    assertEquals("食費", attribute.expenseAttributeName().value());
    assertEquals(ExpenseCategory.変動費, attribute.expenseCategory());
    assertEquals(1L, attribute.version());
  }

  @Test
  void createで毎回異なるUUIDが生成される() {
    ExpenseAttribute attribute1 =
        ExpenseAttribute.create(new ExpenseAttributeName("食費"), ExpenseCategory.変動費);
    ExpenseAttribute attribute2 =
        ExpenseAttribute.create(new ExpenseAttributeName("食費"), ExpenseCategory.変動費);

    assertNotEquals(
        attribute1.expenseAttributeIdentifier(), attribute2.expenseAttributeIdentifier());
  }

  @Test
  void updateWithで新しい値を持つExpenseAttributeを返す() {
    ExpenseAttribute attribute =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費,
            2L);

    ExpenseAttribute updated =
        attribute.updateWith(new ExpenseAttributeName("交通費"), ExpenseCategory.変動費);

    assertEquals("attr-1", updated.expenseAttributeIdentifier().value());
    assertEquals("交通費", updated.expenseAttributeName().value());
    assertEquals(2L, updated.version());
    assertTrue(attribute.hasChanges(updated));
  }

  @Test
  void updateWithで同じ値の場合はhasChangesがfalseを返す() {
    ExpenseAttribute attribute =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費,
            1L);

    ExpenseAttribute updated =
        attribute.updateWith(new ExpenseAttributeName("食費"), ExpenseCategory.変動費);

    assertFalse(attribute.hasChanges(updated));
  }
}
