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

class IncomeRegistrationServiceTest {

  InMemoryIncomeRepository incomeRepository = new InMemoryIncomeRepository();
  InMemoryIncomeAttributeRepository attributeRepository = new InMemoryIncomeAttributeRepository();
  IncomeRegistrationService sut =
      new IncomeRegistrationService(incomeRepository, attributeRepository);

  @Test
  void 収入を作成して登録する() {
    IncomeAttribute attribute =
        new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));
    attributeRepository.register(attribute);

    IncomeIdentifier result =
        sut.createAndRegister(
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            new IncomeAttributeIdentifier("attr-1"));

    assertNotNull(result);
    assertEquals(1, incomeRepository.all().size());
    Income registered = incomeRepository.all().getFirst();
    assertEquals("4月給与", registered.description().value());
    assertEquals(300000, registered.amount().value());
    assertEquals(attribute, registered.incomeAttribute());
    assertEquals(1L, registered.version());
  }
}
