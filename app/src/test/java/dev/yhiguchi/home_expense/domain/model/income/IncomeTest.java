package dev.yhiguchi.home_expense.domain.model.income;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.Amount;
import dev.yhiguchi.home_expense.domain.model.Description;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class IncomeTest {

  IncomeAttributeIdentifier attributeId = new IncomeAttributeIdentifier("attr-1");

  @Test
  void createでUUIDが生成されたIncomeが作成される() {
    Income income =
        Income.create(
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attributeId);

    assertNotNull(income.incomeIdentifier().value());
    assertEquals("4月給与", income.description().value());
    assertEquals(300000, income.amount().value());
    assertEquals("2026-04-25", income.receiveDate().asString());
    assertEquals(attributeId, income.incomeAttributeIdentifier());
  }

  @Test
  void createで毎回異なるUUIDが生成される() {
    Income income1 =
        Income.create(
            new Description("test"),
            new Amount(100),
            new ReceiveDate(LocalDate.of(2026, 1, 1)),
            attributeId);
    Income income2 =
        Income.create(
            new Description("test"),
            new Amount(100),
            new ReceiveDate(LocalDate.of(2026, 1, 1)),
            attributeId);

    assertNotEquals(income1.incomeIdentifier(), income2.incomeIdentifier());
  }

  @Test
  void updateWithで新しい値を持つIncomeを返す() {
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attributeId);

    Income updated =
        income.updateWith(
            new Description("5月給与"),
            new Amount(310000),
            new ReceiveDate(LocalDate.of(2026, 5, 25)),
            attributeId);

    assertEquals("inc-1", updated.incomeIdentifier().value());
    assertEquals("5月給与", updated.description().value());
    assertEquals(310000, updated.amount().value());
    assertTrue(income.hasChanges(updated));
  }

  @Test
  void updateWithで同じ値の場合はhasChangesがfalseを返す() {
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attributeId);

    Income updated =
        income.updateWith(
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attributeId);

    assertFalse(income.hasChanges(updated));
  }

  @Test
  void hasChangesはdescriptionのみ変更された場合trueを返す() {
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attributeId);

    Income updated =
        income.updateWith(
            new Description("5月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attributeId);

    assertTrue(income.hasChanges(updated));
  }

  @Test
  void hasChangesはamountのみ変更された場合trueを返す() {
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attributeId);

    Income updated =
        income.updateWith(
            new Description("4月給与"),
            new Amount(310000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attributeId);

    assertTrue(income.hasChanges(updated));
  }

  @Test
  void hasChangesはreceiveDateのみ変更された場合trueを返す() {
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attributeId);

    Income updated =
        income.updateWith(
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 5, 25)),
            attributeId);

    assertTrue(income.hasChanges(updated));
  }

  @Test
  void hasChangesはincomeAttributeIdentifierのみ変更された場合trueを返す() {
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attributeId);

    IncomeAttributeIdentifier otherAttributeId = new IncomeAttributeIdentifier("attr-2");
    Income updated =
        income.updateWith(
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            otherAttributeId);

    assertTrue(income.hasChanges(updated));
  }
}
