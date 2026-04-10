package dev.yhiguchi.home_expense.domain.model.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExpenseCreatorTest {

  @Test
  void 経費を作成して登録する() {
    ExpenseAttribute attribute =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費);
    List<Expense> registered = new ArrayList<>();

    ExpenseCreator creator = new ExpenseCreator(identifier -> attribute, registered::add);

    Expense expense =
        creator.create(
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 10)),
            new ExpenseAttributeIdentifier("attr-1"));

    assertNotNull(expense.expenseIdentifier().value());
    assertEquals("ランチ", expense.description().value());
    assertEquals(1000, expense.price().value());
    assertEquals(attribute, expense.expenseAttribute());
    assertEquals(1, registered.size());
    assertSame(expense, registered.getFirst());
  }

  @Test
  void UUIDが生成される() {
    ExpenseAttribute attribute =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費);

    ExpenseCreator creator = new ExpenseCreator(identifier -> attribute, expense -> {});

    Expense expense1 =
        creator.create(
            new Description("test"),
            new Price(100),
            new PaymentDate(LocalDate.of(2026, 1, 1)),
            new ExpenseAttributeIdentifier("attr-1"));
    Expense expense2 =
        creator.create(
            new Description("test"),
            new Price(100),
            new PaymentDate(LocalDate.of(2026, 1, 1)),
            new ExpenseAttributeIdentifier("attr-1"));

    assertNotEquals(expense1.expenseIdentifier(), expense2.expenseIdentifier());
  }
}
