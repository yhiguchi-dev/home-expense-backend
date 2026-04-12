package dev.yhiguchi.home_expense.domain.model.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ExpenseTest {

  @Test
  void 固定費の場合isFixedがtrueを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("家賃"),
            new Price(80000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            new ExpenseAttribute(
                new ExpenseAttributeIdentifier("attr-1"),
                new ExpenseAttributeName("家賃"),
                ExpenseCategory.固定費),
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
            new ExpenseAttribute(
                new ExpenseAttributeIdentifier("attr-1"),
                new ExpenseAttributeName("食費"),
                ExpenseCategory.変動費),
            1L);

    assertFalse(expense.isFixed());
    assertTrue(expense.isVariable());
  }

  @Test
  void 属性未設定の場合どちらもfalseを返す() {
    Expense expense = new Expense();

    assertFalse(expense.isFixed());
    assertFalse(expense.isVariable());
  }

  @Test
  void 同値のExpenseはequalsがtrueを返す() {
    ExpenseAttribute attribute =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費);
    Expense expense1 =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attribute,
            1L);
    Expense expense2 =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attribute,
            1L);

    assertEquals(expense1, expense2);
    assertEquals(expense1.hashCode(), expense2.hashCode());
  }
}
