package dev.yhiguchi.home_expense.domain.model.income;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeName;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class IncomeTest {

  @Test
  void createでUUIDが生成されversion1のIncomeが作成される() {
    IncomeAttribute attribute =
        new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));

    Income income =
        Income.create(
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute);

    assertNotNull(income.incomeIdentifier().value());
    assertEquals("4月給与", income.description().value());
    assertEquals(300000, income.amount().value());
    assertEquals("2026-04-25", income.receiveDate().value());
    assertEquals(attribute, income.incomeAttribute());
    assertEquals(1L, income.version());
  }

  @Test
  void createで毎回異なるUUIDが生成される() {
    IncomeAttribute attribute =
        new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));

    Income income1 =
        Income.create(
            new Description("test"),
            new Amount(100),
            new ReceiveDate(LocalDate.of(2026, 1, 1)),
            attribute);
    Income income2 =
        Income.create(
            new Description("test"),
            new Amount(100),
            new ReceiveDate(LocalDate.of(2026, 1, 1)),
            attribute);

    assertNotEquals(income1.incomeIdentifier(), income2.incomeIdentifier());
  }

  @Test
  void updateWithで新しい値を持つIncomeを返す() {
    IncomeAttribute attribute =
        new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute,
            3L);

    Income updated =
        income.updateWith(
            new Description("5月給与"),
            new Amount(310000),
            new ReceiveDate(LocalDate.of(2026, 5, 25)),
            attribute);

    assertEquals("inc-1", updated.incomeIdentifier().value());
    assertEquals("5月給与", updated.description().value());
    assertEquals(310000, updated.amount().value());
    assertEquals(3L, updated.version());
    assertTrue(income.hasChanges(updated));
  }

  @Test
  void updateWithで同じ値の場合はhasChangesがfalseを返す() {
    IncomeAttribute attribute =
        new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute,
            1L);

    Income updated =
        income.updateWith(
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute);

    assertFalse(income.hasChanges(updated));
  }

  @Test
  void hasChangesはdescriptionのみ変更された場合trueを返す() {
    IncomeAttribute attribute =
        new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute,
            1L);

    Income updated =
        income.updateWith(
            new Description("5月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute);

    assertTrue(income.hasChanges(updated));
  }

  @Test
  void hasChangesはamountのみ変更された場合trueを返す() {
    IncomeAttribute attribute =
        new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute,
            1L);

    Income updated =
        income.updateWith(
            new Description("4月給与"),
            new Amount(310000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute);

    assertTrue(income.hasChanges(updated));
  }

  @Test
  void hasChangesはreceiveDateのみ変更された場合trueを返す() {
    IncomeAttribute attribute =
        new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute,
            1L);

    Income updated =
        income.updateWith(
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 5, 25)),
            attribute);

    assertTrue(income.hasChanges(updated));
  }

  @Test
  void hasChangesはincomeAttributeのみ変更された場合trueを返す() {
    IncomeAttribute attribute =
        new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute,
            1L);

    IncomeAttribute otherAttribute =
        new IncomeAttribute(new IncomeAttributeIdentifier("attr-2"), new IncomeAttributeName("賞与"));
    Income updated =
        income.updateWith(
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            otherAttribute);

    assertTrue(income.hasChanges(updated));
  }
}
