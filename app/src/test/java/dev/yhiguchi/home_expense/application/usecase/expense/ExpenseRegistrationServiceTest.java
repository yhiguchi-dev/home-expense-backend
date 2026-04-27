package dev.yhiguchi.home_expense.application.usecase.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.Amount;
import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeNotFoundException;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryExpenseAttributeRepository;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryExpenseRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ExpenseRegistrationServiceTest {

  InMemoryExpenseRepository expenseRepository = new InMemoryExpenseRepository();
  InMemoryExpenseAttributeRepository attributeRepository = new InMemoryExpenseAttributeRepository();
  ExpenseRegistrationService sut =
      new ExpenseRegistrationService(expenseRepository, attributeRepository);

  @Test
  void 経費を作成して登録する() {
    ExpenseAttribute attribute =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費);
    attributeRepository.register(attribute);

    ExpenseIdentifier result =
        sut.register(
            new ExpenseRegistrationCommand(
                new Description("ランチ"),
                new Amount(1000),
                new PaymentDate(LocalDate.of(2026, 4, 10)),
                new ExpenseAttributeIdentifier("attr-1")));

    assertNotNull(result);
    assertEquals(1, expenseRepository.all().size());
    Expense registered = expenseRepository.all().getFirst();
    assertEquals("ランチ", registered.description().value());
    assertEquals(1000, registered.amount().value());
    assertEquals(new ExpenseAttributeIdentifier("attr-1"), registered.expenseAttributeIdentifier());
  }

  @Test
  void 存在しない属性IDで登録すると例外をスローする() {
    assertThrows(
        ExpenseAttributeNotFoundException.class,
        () ->
            sut.register(
                new ExpenseRegistrationCommand(
                    new Description("ランチ"),
                    new Amount(1000),
                    new PaymentDate(LocalDate.of(2026, 4, 10)),
                    new ExpenseAttributeIdentifier("not-exist"))));
  }
}
