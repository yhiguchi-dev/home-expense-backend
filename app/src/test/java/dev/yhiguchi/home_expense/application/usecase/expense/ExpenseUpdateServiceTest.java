package dev.yhiguchi.home_expense.application.usecase.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryExpenseAttributeRepository;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryExpenseRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ExpenseUpdateServiceTest {

  InMemoryExpenseRepository expenseRepository = new InMemoryExpenseRepository();
  InMemoryExpenseAttributeRepository attributeRepository = new InMemoryExpenseAttributeRepository();
  ExpenseUpdateService sut = new ExpenseUpdateService(expenseRepository, attributeRepository);

  ExpenseAttribute attribute =
      new ExpenseAttribute(
          new ExpenseAttributeIdentifier("attr-1"),
          new ExpenseAttributeName("食費"),
          ExpenseCategory.変動費);

  @Test
  void 経費を更新する() {
    Expense existing =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attribute,
            1L);
    expenseRepository.register(existing);
    attributeRepository.register(attribute);

    sut.update(
        new ExpenseIdentifier("exp-1"),
        new Description("ディナー"),
        new Price(2000),
        new PaymentDate(LocalDate.of(2026, 4, 2)),
        new ExpenseAttributeIdentifier("attr-1"),
        1L);

    Expense updated = expenseRepository.get(new ExpenseIdentifier("exp-1"));
    assertEquals("ディナー", updated.description().value());
    assertEquals(2000, updated.price().value());
    assertEquals(1L, updated.version());
  }

  @Test
  void 変更がない場合は更新しない() {
    Expense existing =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            attribute,
            1L);
    expenseRepository.register(existing);
    attributeRepository.register(attribute);

    sut.update(
        new ExpenseIdentifier("exp-1"),
        new Description("ランチ"),
        new Price(1000),
        new PaymentDate(LocalDate.of(2026, 4, 1)),
        new ExpenseAttributeIdentifier("attr-1"),
        1L);

    Expense result = expenseRepository.get(new ExpenseIdentifier("exp-1"));
    assertSame(existing, result);
  }
}
