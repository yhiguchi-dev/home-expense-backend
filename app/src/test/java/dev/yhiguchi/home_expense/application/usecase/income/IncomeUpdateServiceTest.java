package dev.yhiguchi.home_expense.application.usecase.income;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeName;
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
            attribute,
            1L);
    incomeRepository.register(existing);
    attributeRepository.register(attribute);

    sut.update(
        new IncomeIdentifier("inc-1"),
        new Description("5月給与"),
        new Amount(310000),
        new ReceiveDate(LocalDate.of(2026, 5, 25)),
        new IncomeAttributeIdentifier("attr-1"),
        1L);

    Income updated = incomeRepository.get(new IncomeIdentifier("inc-1"));
    assertEquals("5月給与", updated.description().value());
    assertEquals(310000, updated.amount().value());
    assertEquals(1L, updated.version());
  }

  @Test
  void 変更がない場合は更新しない() {
    Income existing =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute,
            1L);
    incomeRepository.register(existing);
    attributeRepository.register(attribute);

    sut.update(
        new IncomeIdentifier("inc-1"),
        new Description("4月給与"),
        new Amount(300000),
        new ReceiveDate(LocalDate.of(2026, 4, 25)),
        new IncomeAttributeIdentifier("attr-1"),
        1L);

    Income result = incomeRepository.get(new IncomeIdentifier("inc-1"));
    assertSame(existing, result);
  }
}
