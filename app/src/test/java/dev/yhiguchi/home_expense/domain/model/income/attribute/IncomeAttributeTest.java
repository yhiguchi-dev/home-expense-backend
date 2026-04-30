package dev.yhiguchi.home_expense.domain.model.income.attribute;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IncomeAttributeTest {

  @Test
  void createでUUIDが生成されたIncomeAttributeが作成される() {
    IncomeAttribute attribute = IncomeAttribute.create(new IncomeAttributeName("給与"));

    assertNotNull(attribute.incomeAttributeIdentifier().value());
    assertEquals("給与", attribute.incomeAttributeName().value());
  }

  @Test
  void createで毎回異なるUUIDが生成される() {
    IncomeAttribute attribute1 = IncomeAttribute.create(new IncomeAttributeName("給与"));
    IncomeAttribute attribute2 = IncomeAttribute.create(new IncomeAttributeName("給与"));

    assertNotEquals(attribute1.incomeAttributeIdentifier(), attribute2.incomeAttributeIdentifier());
  }

  @Test
  void updateWithで新しい値を持つIncomeAttributeを返す() {
    IncomeAttribute attribute =
        new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));

    IncomeAttribute updated = attribute.updateWith(new IncomeAttributeName("賞与"));

    assertEquals("attr-1", updated.incomeAttributeIdentifier().value());
    assertEquals("賞与", updated.incomeAttributeName().value());
    assertTrue(attribute.hasChanges(updated));
  }

  @Test
  void updateWithで同じ値の場合はhasChangesがfalseを返す() {
    IncomeAttribute attribute =
        new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));

    IncomeAttribute updated = attribute.updateWith(new IncomeAttributeName("給与"));

    assertFalse(attribute.hasChanges(updated));
  }
}
