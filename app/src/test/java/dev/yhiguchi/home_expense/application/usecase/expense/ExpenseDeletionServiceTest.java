package dev.yhiguchi.home_expense.application.usecase.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryExpenseRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ExpenseDeletionServiceTest {

  InMemoryExpenseRepository expenseRepository = new InMemoryExpenseRepository();
  ExpenseDeletionService sut = new ExpenseDeletionService(expenseRepository);

  @Test
  void 経費を削除する() {
    Expense existing =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            new ExpenseAttributeIdentifier("attr-1"),
            ExpenseCategory.変動費,
            1L);
    expenseRepository.register(existing);

    sut.delete(new ExpenseIdentifier("exp-1"));

    assertTrue(expenseRepository.all().isEmpty());
  }

  @Test
  void 存在しない経費を削除すると例外をスローする() {
    assertThrows(
        ExpenseNotFoundException.class, () -> sut.delete(new ExpenseIdentifier("not-exist")));
  }
}
