package dev.yhiguchi.home_expense.application.usecase.income;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryIncomeAttributeRepository;
import org.junit.jupiter.api.Test;

class IncomeAttributeRegistrationServiceTest {

  InMemoryIncomeAttributeRepository attributeRepository = new InMemoryIncomeAttributeRepository();
  IncomeAttributeRegistrationService sut =
      new IncomeAttributeRegistrationService(attributeRepository);

  @Test
  void 収入属性を作成して登録する() {
    IncomeAttributeIdentifier result = sut.createAndRegister(new IncomeAttributeName("給与"));

    assertNotNull(result);
    assertEquals(1, attributeRepository.all().size());
    IncomeAttribute registered = attributeRepository.all().getFirst();
    assertEquals("給与", registered.incomeAttributeName().value());
    assertEquals(1L, registered.version());
  }

  @Test
  void 同名の収入属性が存在する場合は例外をスローする() {
    IncomeAttribute existing =
        new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));
    attributeRepository.register(existing);

    assertThrows(
        IncomeAttributeAlreadyExistsException.class,
        () -> sut.createAndRegister(new IncomeAttributeName("給与")));
  }
}
