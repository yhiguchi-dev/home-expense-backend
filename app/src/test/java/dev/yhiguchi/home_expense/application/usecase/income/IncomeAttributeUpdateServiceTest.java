package dev.yhiguchi.home_expense.application.usecase.income;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryIncomeAttributeRepository;
import org.junit.jupiter.api.Test;

class IncomeAttributeUpdateServiceTest {

  InMemoryIncomeAttributeRepository attributeRepository = new InMemoryIncomeAttributeRepository();
  IncomeAttributeUpdateService sut = new IncomeAttributeUpdateService(attributeRepository);

  @Test
  void 収入属性を更新する() {
    IncomeAttribute existing =
        new IncomeAttribute(
            new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"), 1L);
    attributeRepository.register(existing);

    sut.update(
        new IncomeAttributeUpdateCommand(
            new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("賞与"), 1L));

    IncomeAttribute updated =
        attributeRepository.findBy(new IncomeAttributeIdentifier("attr-1")).orElseThrow();
    assertEquals("賞与", updated.incomeAttributeName().value());
    assertEquals(1L, updated.version());
  }

  @Test
  void 変更がない場合は更新しない() {
    IncomeAttribute existing =
        new IncomeAttribute(
            new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"), 1L);
    attributeRepository.register(existing);

    sut.update(
        new IncomeAttributeUpdateCommand(
            new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"), 1L));

    IncomeAttribute result =
        attributeRepository.findBy(new IncomeAttributeIdentifier("attr-1")).orElseThrow();
    assertSame(existing, result);
  }
}
