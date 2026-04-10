package dev.yhiguchi.home_expense.domain.model.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExpensesTest {

  ExpenseAttribute attribute =
      new ExpenseAttribute(
          new ExpenseAttributeIdentifier("attr-1"),
          new ExpenseAttributeName("食費"),
          ExpenseCategory.変動費);

  @Test
  void 一致する属性を持つ経費がある場合trueを返す() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attribute);
    Expenses expenses = new Expenses(List.of(expense));

    assertTrue(expenses.has(attribute));
  }

  @Test
  void 一致する属性を持つ経費がない場合falseを返す() {
    ExpenseAttribute otherAttribute =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-2"),
            new ExpenseAttributeName("交通費"),
            ExpenseCategory.変動費);
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            otherAttribute);
    Expenses expenses = new Expenses(List.of(expense));

    assertFalse(expenses.has(attribute));
  }

  @Test
  void 空の場合falseを返す() {
    Expenses expenses = new Expenses();

    assertFalse(expenses.has(attribute));
  }
}
