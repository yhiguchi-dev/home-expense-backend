package dev.yhiguchi.home_expense.application.usecase.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.Amount;
import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeNotFoundException;
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
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            new ExpenseAttributeIdentifier("attr-1"));
    expenseRepository.register(existing);
    attributeRepository.register(attribute);

    sut.update(
        new ExpenseUpdateCommand(
            new ExpenseIdentifier("exp-1"),
            new Description("ディナー"),
            new Amount(2000),
            new PaymentDate(LocalDate.of(2026, 4, 2)),
            new ExpenseAttributeIdentifier("attr-1"),
            1L));

    Revision<Expense> updated =
        expenseRepository.findBy(new ExpenseIdentifier("exp-1")).orElseThrow();
    assertEquals("ディナー", updated.entity().description().value());
    assertEquals(2000, updated.entity().amount().value());
    assertEquals(2L, updated.version());
  }

  @Test
  void 変更がない場合は更新しない() {
    Expense existing =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            new ExpenseAttributeIdentifier("attr-1"));
    expenseRepository.register(existing);
    attributeRepository.register(attribute);

    sut.update(
        new ExpenseUpdateCommand(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            new ExpenseAttributeIdentifier("attr-1"),
            1L));

    Revision<Expense> result =
        expenseRepository.findBy(new ExpenseIdentifier("exp-1")).orElseThrow();
    assertSame(existing, result.entity());
    assertEquals(1L, result.version());
  }

  @Test
  void 存在しない経費を更新すると例外をスローする() {
    attributeRepository.register(attribute);

    assertThrows(
        ExpenseNotFoundException.class,
        () ->
            sut.update(
                new ExpenseUpdateCommand(
                    new ExpenseIdentifier("not-exist"),
                    new Description("ランチ"),
                    new Amount(1000),
                    new PaymentDate(LocalDate.of(2026, 4, 1)),
                    new ExpenseAttributeIdentifier("attr-1"),
                    1L)));
  }

  @Test
  void 存在しない属性IDで更新すると例外をスローする() {
    Expense existing =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Amount(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            new ExpenseAttributeIdentifier("attr-1"));
    expenseRepository.register(existing);

    assertThrows(
        ExpenseAttributeNotFoundException.class,
        () ->
            sut.update(
                new ExpenseUpdateCommand(
                    new ExpenseIdentifier("exp-1"),
                    new Description("ディナー"),
                    new Amount(2000),
                    new PaymentDate(LocalDate.of(2026, 4, 2)),
                    new ExpenseAttributeIdentifier("not-exist"),
                    1L)));
  }
}
