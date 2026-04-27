package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import org.junit.jupiter.api.Test;

class ExpenseAttributeTest {

  @Test
  void createでUUIDが生成されたExpenseAttributeが作成される() {
    ExpenseAttribute attribute =
        ExpenseAttribute.create(new ExpenseAttributeName("食費"), ExpenseCategory.変動費);

    assertNotNull(attribute.expenseAttributeIdentifier().value());
    assertEquals("食費", attribute.expenseAttributeName().value());
    assertEquals(ExpenseCategory.変動費, attribute.expenseCategory());
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
  void updateWithで新しい名前を持つExpenseAttributeを返す() {
    ExpenseAttribute attribute =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費);

    ExpenseAttribute updated = attribute.updateWith(new ExpenseAttributeName("交通費"));

    assertEquals("attr-1", updated.expenseAttributeIdentifier().value());
    assertEquals("交通費", updated.expenseAttributeName().value());
    assertEquals(ExpenseCategory.変動費, updated.expenseCategory());
    assertTrue(attribute.hasChanges(updated));
  }

  @Test
  void updateWithで同じ名前の場合はhasChangesがfalseを返す() {
    ExpenseAttribute attribute =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費);

    ExpenseAttribute updated = attribute.updateWith(new ExpenseAttributeName("食費"));

    assertFalse(attribute.hasChanges(updated));
  }

  @Test
  void 固定費の場合isFixedがtrueを返す() {
    ExpenseAttribute attribute =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("家賃"),
            ExpenseCategory.固定費);

    assertTrue(attribute.isFixed());
    assertFalse(attribute.isVariable());
  }

  @Test
  void 変動費の場合isVariableがtrueを返す() {
    ExpenseAttribute attribute =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費);

    assertFalse(attribute.isFixed());
    assertTrue(attribute.isVariable());
  }
}
