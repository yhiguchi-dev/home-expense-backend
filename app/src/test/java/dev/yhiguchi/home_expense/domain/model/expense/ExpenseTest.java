package dev.yhiguchi.home_expense.domain.model.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.Amount;
import dev.yhiguchi.home_expense.domain.model.Description;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ExpenseTest {

  ExpenseAttributeIdentifier attributeId = new ExpenseAttributeIdentifier("attr-1");

  @Test
  void 同一識別子のExpenseはequalsがtrueを返す() {
    Expense expense1 =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId);
    Expense expense2 =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ディナー"),
            new Amount(2000),
            new PaymentDate(LocalDate.of(2026, 4, 2)),
            attributeId);

    assertEquals(expense1, expense2);
    assertEquals(expense1.hashCode(), expense2.hashCode());
  }

  @Test
  void createでUUIDが生成されたExpenseが作成される() {
    Expense expense =
        Expense.create(
            new Description("ランチ"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 10)),
            attributeId);

    assertNotNull(expense.expenseIdentifier().value());
    assertEquals("ランチ", expense.description().value());
    assertEquals(1000, expense.amount().value());
    assertEquals("2026-04-10", expense.paymentDate().asString());
    assertEquals(attributeId, expense.expenseAttributeIdentifier());
  }

  @Test
  void createで毎回異なるUUIDが生成される() {
    Expense expense1 =
        Expense.create(
            new Description("test"),
            new Amount(100),
            new PaymentDate(LocalDate.of(2026, 1, 1)),
            attributeId);
    Expense expense2 =
        Expense.create(
            new Description("test"),
            new Amount(100),
            new PaymentDate(LocalDate.of(2026, 1, 1)),
            attributeId);

    assertNotEquals(expense1.expenseIdentifier(), expense2.expenseIdentifier());
  }

  @Test
  void updateWithで新しい値を持つExpenseを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId);

    Expense updated =
        expense.updateWith(
            new Description("ディナー"),
            new Amount(2000),
            new PaymentDate(LocalDate.of(2026, 4, 2)),
            attributeId);

    assertEquals("exp-1", updated.expenseIdentifier().value());
    assertEquals("ディナー", updated.description().value());
    assertEquals(2000, updated.amount().value());
    assertTrue(expense.hasChanges(updated));
  }

  @Test
  void updateWithで同じ値の場合はhasChangesがfalseを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId);

    Expense updated =
        expense.updateWith(
            new Description("ランチ"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId);

    assertFalse(expense.hasChanges(updated));
  }

  @Test
  void hasChangesはdescriptionのみ変更された場合trueを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId);

    Expense updated =
        expense.updateWith(
            new Description("ディナー"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId);

    assertTrue(expense.hasChanges(updated));
  }

  @Test
  void hasChangesはpriceのみ変更された場合trueを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId);

    Expense updated =
        expense.updateWith(
            new Description("ランチ"),
            new Amount(2000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId);

    assertTrue(expense.hasChanges(updated));
  }

  @Test
  void hasChangesはpaymentDateのみ変更された場合trueを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId);

    Expense updated =
        expense.updateWith(
            new Description("ランチ"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 2)),
            attributeId);

    assertTrue(expense.hasChanges(updated));
  }

  @Test
  void hasChangesはexpenseAttributeIdentifierのみ変更された場合trueを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId);

    ExpenseAttributeIdentifier otherAttributeId = new ExpenseAttributeIdentifier("attr-2");
    Expense updated =
        expense.updateWith(
            new Description("ランチ"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            otherAttributeId);

    assertTrue(expense.hasChanges(updated));
  }
}
