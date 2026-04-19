package dev.yhiguchi.home_expense.domain.model.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ExpenseTest {

  ExpenseAttributeIdentifier attributeId = new ExpenseAttributeIdentifier("attr-1");

  @Test
  void 固定費の場合isFixedがtrueを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("家賃"),
            new Price(80000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId,
            ExpenseCategory.固定費,
            1L);

    assertTrue(expense.isFixed());
    assertFalse(expense.isVariable());
  }

  @Test
  void 変動費の場合isVariableがtrueを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("食費"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId,
            ExpenseCategory.変動費,
            1L);

    assertFalse(expense.isFixed());
    assertTrue(expense.isVariable());
  }

  @Test
  void 同一識別子のExpenseはequalsがtrueを返す() {
    Expense expense1 =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId,
            ExpenseCategory.変動費,
            1L);
    Expense expense2 =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ディナー"),
            new Price(2000),
            new PaymentDate(LocalDate.of(2026, 4, 2)),
            attributeId,
            ExpenseCategory.変動費,
            2L);

    assertEquals(expense1, expense2);
    assertEquals(expense1.hashCode(), expense2.hashCode());
  }

  @Test
  void createでUUIDが生成されversion1のExpenseが作成される() {
    Expense expense =
        Expense.create(
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 10)),
            attributeId,
            ExpenseCategory.変動費);

    assertNotNull(expense.expenseIdentifier().value());
    assertEquals("ランチ", expense.description().value());
    assertEquals(1000, expense.price().value());
    assertEquals("2026-04-10", expense.paymentDate().asString());
    assertEquals(attributeId, expense.expenseAttributeIdentifier());
    assertEquals(ExpenseCategory.変動費, expense.expenseCategory());
    assertEquals(1L, expense.version());
  }

  @Test
  void createで毎回異なるUUIDが生成される() {
    Expense expense1 =
        Expense.create(
            new Description("test"),
            new Price(100),
            new PaymentDate(LocalDate.of(2026, 1, 1)),
            attributeId,
            ExpenseCategory.変動費);
    Expense expense2 =
        Expense.create(
            new Description("test"),
            new Price(100),
            new PaymentDate(LocalDate.of(2026, 1, 1)),
            attributeId,
            ExpenseCategory.変動費);

    assertNotEquals(expense1.expenseIdentifier(), expense2.expenseIdentifier());
  }

  @Test
  void updateWithで新しい値を持つExpenseを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId,
            ExpenseCategory.変動費,
            3L);

    Expense updated =
        expense.updateWith(
            new Description("ディナー"),
            new Price(2000),
            new PaymentDate(LocalDate.of(2026, 4, 2)),
            attributeId,
            ExpenseCategory.変動費);

    assertEquals("exp-1", updated.expenseIdentifier().value());
    assertEquals("ディナー", updated.description().value());
    assertEquals(2000, updated.price().value());
    assertEquals(3L, updated.version());
    assertTrue(expense.hasChanges(updated));
  }

  @Test
  void updateWithで同じ値の場合はhasChangesがfalseを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId,
            ExpenseCategory.変動費,
            1L);

    Expense updated =
        expense.updateWith(
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId,
            ExpenseCategory.変動費);

    assertFalse(expense.hasChanges(updated));
  }

  @Test
  void hasChangesはdescriptionのみ変更された場合trueを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId,
            ExpenseCategory.変動費,
            1L);

    Expense updated =
        expense.updateWith(
            new Description("ディナー"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId,
            ExpenseCategory.変動費);

    assertTrue(expense.hasChanges(updated));
  }

  @Test
  void hasChangesはpriceのみ変更された場合trueを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId,
            ExpenseCategory.変動費,
            1L);

    Expense updated =
        expense.updateWith(
            new Description("ランチ"),
            new Price(2000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId,
            ExpenseCategory.変動費);

    assertTrue(expense.hasChanges(updated));
  }

  @Test
  void hasChangesはpaymentDateのみ変更された場合trueを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId,
            ExpenseCategory.変動費,
            1L);

    Expense updated =
        expense.updateWith(
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 2)),
            attributeId,
            ExpenseCategory.変動費);

    assertTrue(expense.hasChanges(updated));
  }

  @Test
  void hasChangesはexpenseAttributeIdentifierのみ変更された場合trueを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attributeId,
            ExpenseCategory.変動費,
            1L);

    ExpenseAttributeIdentifier otherAttributeId = new ExpenseAttributeIdentifier("attr-2");
    Expense updated =
        expense.updateWith(
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            otherAttributeId,
            ExpenseCategory.変動費);

    assertTrue(expense.hasChanges(updated));
  }
}
