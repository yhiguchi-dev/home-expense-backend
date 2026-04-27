package dev.yhiguchi.home_expense.application.usecase.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryExpenseAttributeRepository;
import org.junit.jupiter.api.Test;

class ExpenseAttributeRegistrationServiceTest {

  InMemoryExpenseAttributeRepository attributeRepository = new InMemoryExpenseAttributeRepository();
  ExpenseAttributeRegistrationService sut =
      new ExpenseAttributeRegistrationService(attributeRepository);

  @Test
  void 経費属性を作成して登録する() {
    ExpenseAttributeIdentifier result =
        sut.register(
            new ExpenseAttributeRegistrationCommand(
                new ExpenseAttributeName("食費"), ExpenseCategory.変動費));

    assertNotNull(result);
    assertEquals(1, attributeRepository.all().size());
    ExpenseAttribute registered = attributeRepository.all().getFirst();
    assertEquals("食費", registered.expenseAttributeName().value());
    assertEquals(ExpenseCategory.変動費, registered.expenseCategory());
  }

  @Test
  void 同名の経費属性が存在する場合は例外をスローする() {
    ExpenseAttribute existing =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費);
    attributeRepository.register(existing);

    assertThrows(
        ExpenseAttributeAlreadyExistsException.class,
        () ->
            sut.register(
                new ExpenseAttributeRegistrationCommand(
                    new ExpenseAttributeName("食費"), ExpenseCategory.変動費)));
  }
}
