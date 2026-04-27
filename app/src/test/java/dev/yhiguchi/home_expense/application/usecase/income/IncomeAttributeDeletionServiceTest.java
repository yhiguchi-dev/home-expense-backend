package dev.yhiguchi.home_expense.application.usecase.income;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeConstraintException;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeName;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryIncomeAttributeRepository;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryIncomeRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class IncomeAttributeDeletionServiceTest {

  InMemoryIncomeAttributeRepository attributeRepository = new InMemoryIncomeAttributeRepository();
  InMemoryIncomeRepository incomeRepository = new InMemoryIncomeRepository();
  IncomeAttributeDeletionService sut =
      new IncomeAttributeDeletionService(attributeRepository, incomeRepository);

  IncomeAttribute attribute =
      new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));

  @Test
  void 収入属性を削除する() {
    attributeRepository.register(attribute);

    sut.delete(new IncomeAttributeIdentifier("attr-1"));

    assertTrue(attributeRepository.all().isEmpty());
  }

  @Test
  void 参照する収入がある場合は例外をスローする() {
    attributeRepository.register(attribute);
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            new IncomeAttributeIdentifier("attr-1"));
    incomeRepository.register(income);

    assertThrows(
        IncomeAttributeConstraintException.class,
        () -> sut.delete(new IncomeAttributeIdentifier("attr-1")));
    assertEquals(1, attributeRepository.all().size());
  }
}
