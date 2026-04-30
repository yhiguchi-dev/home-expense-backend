package dev.yhiguchi.home_expense.application.usecase.income;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.Description;
import dev.yhiguchi.home_expense.domain.model.Amount;
import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryIncomeAttributeRepository;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryIncomeRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class IncomeUpdateServiceTest {

  InMemoryIncomeRepository incomeRepository = new InMemoryIncomeRepository();
  InMemoryIncomeAttributeRepository attributeRepository = new InMemoryIncomeAttributeRepository();
  IncomeUpdateService sut = new IncomeUpdateService(incomeRepository, attributeRepository);

  IncomeAttribute attribute =
      new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));

  @Test
  void 収入を更新する() {
    Income existing =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            new IncomeAttributeIdentifier("attr-1"));
    incomeRepository.register(existing);
    attributeRepository.register(attribute);

    sut.update(
        new IncomeUpdateCommand(
            new IncomeIdentifier("inc-1"),
            new Description("5月給与"),
            new Amount(310000),
            new ReceiveDate(LocalDate.of(2026, 5, 25)),
            new IncomeAttributeIdentifier("attr-1"),
            1L));

    Income updated = incomeRepository.find(new IncomeIdentifier("inc-1")).orElseThrow();
    assertEquals("5月給与", updated.description().value());
    assertEquals(310000, updated.amount().value());
    assertEquals(2L, incomeRepository.versionOf(new IncomeIdentifier("inc-1")));
  }

  @Test
  void 変更がない場合は更新しない() {
    Income existing =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            new IncomeAttributeIdentifier("attr-1"));
    incomeRepository.register(existing);
    attributeRepository.register(attribute);

    sut.update(
        new IncomeUpdateCommand(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            new IncomeAttributeIdentifier("attr-1"),
            1L));

    Income result = incomeRepository.find(new IncomeIdentifier("inc-1")).orElseThrow();
    assertSame(existing, result);
    assertEquals(1L, incomeRepository.versionOf(new IncomeIdentifier("inc-1")));
  }

  @Test
  void 存在しない収入を更新すると例外をスローする() {
    attributeRepository.register(attribute);

    assertThrows(
        IncomeNotFoundException.class,
        () ->
            sut.update(
                new IncomeUpdateCommand(
                    new IncomeIdentifier("not-exist"),
                    new Description("4月給与"),
                    new Amount(300000),
                    new ReceiveDate(LocalDate.of(2026, 4, 25)),
                    new IncomeAttributeIdentifier("attr-1"),
                    1L)));
  }

  @Test
  void 存在しない属性IDで更新すると例外をスローする() {
    Income existing =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            new IncomeAttributeIdentifier("attr-1"));
    incomeRepository.register(existing);

    assertThrows(
        IncomeAttributeNotFoundException.class,
        () ->
            sut.update(
                new IncomeUpdateCommand(
                    new IncomeIdentifier("inc-1"),
                    new Description("5月給与"),
                    new Amount(310000),
                    new ReceiveDate(LocalDate.of(2026, 5, 25)),
                    new IncomeAttributeIdentifier("not-exist"),
                    1L)));
  }
}
