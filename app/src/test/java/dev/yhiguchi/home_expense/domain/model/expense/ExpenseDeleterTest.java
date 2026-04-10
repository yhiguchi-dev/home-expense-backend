package dev.yhiguchi.home_expense.domain.model.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExpenseDeleterTest {

  @Test
  void 経費を削除する() {
    Expense existing =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            new ExpenseAttribute(
                new ExpenseAttributeIdentifier("attr-1"),
                new ExpenseAttributeName("食費"),
                ExpenseCategory.変動費));

    List<ExpenseIdentifier> deleted = new ArrayList<>();

    ExpenseDeleter deleter = new ExpenseDeleter(id -> existing, deleted::add);
    deleter.delete(new ExpenseIdentifier("exp-1"));

    assertEquals(1, deleted.size());
    assertEquals("exp-1", deleted.getFirst().value());
  }
}
