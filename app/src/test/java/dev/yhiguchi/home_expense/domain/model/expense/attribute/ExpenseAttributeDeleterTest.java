package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExpenseAttributeDeleterTest {

  ExpenseAttribute attribute =
      new ExpenseAttribute(
          new ExpenseAttributeIdentifier("attr-1"),
          new ExpenseAttributeName("食費"),
          ExpenseCategory.変動費);

  @Test
  void 経費属性を削除する() {
    List<ExpenseAttributeIdentifier> deleted = new ArrayList<>();
    Expenses emptyExpenses = new Expenses();

    ExpenseAttributeDeleter deleter =
        new ExpenseAttributeDeleter(id -> attribute, deleted::add, attr -> emptyExpenses);

    deleter.delete(new ExpenseAttributeIdentifier("attr-1"));

    assertEquals(1, deleted.size());
    assertEquals("attr-1", deleted.getFirst().value());
  }

  @Test
  void 参照する経費がある場合は例外をスローする() {
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attribute,
            1L);
    Expenses expensesWithAttribute = new Expenses(List.of(expense));

    ExpenseAttributeDeleter deleter =
        new ExpenseAttributeDeleter(id -> attribute, id -> {}, attr -> expensesWithAttribute);

    assertThrows(
        ExpenseAttributeConstraintException.class,
        () -> deleter.delete(new ExpenseAttributeIdentifier("attr-1")));
  }
}
