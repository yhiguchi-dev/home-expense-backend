package dev.yhiguchi.home_expense.application.usecase.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeConstraintException;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryExpenseAttributeRepository;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryExpenseRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ExpenseAttributeDeletionServiceTest {

  InMemoryExpenseAttributeRepository attributeRepository = new InMemoryExpenseAttributeRepository();
  InMemoryExpenseRepository expenseRepository = new InMemoryExpenseRepository();
  ExpenseAttributeDeletionService sut =
      new ExpenseAttributeDeletionService(attributeRepository, expenseRepository);

  ExpenseAttribute attribute =
      new ExpenseAttribute(
          new ExpenseAttributeIdentifier("attr-1"),
          new ExpenseAttributeName("食費"),
          ExpenseCategory.変動費);

  @Test
  void 経費属性を削除する() {
    attributeRepository.register(attribute);

    sut.delete(new ExpenseAttributeIdentifier("attr-1"));

    assertTrue(attributeRepository.all().isEmpty());
  }

  @Test
  void 参照する経費がある場合は例外をスローする() {
    attributeRepository.register(attribute);
    Expense expense =
        new Expense(
            new ExpenseIdentifier("exp-1"),
            new Description("ランチ"),
            new Price(1000),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            new ExpenseAttributeIdentifier("attr-1"),
            ExpenseCategory.変動費);
    expenseRepository.register(expense);

    assertThrows(
        ExpenseAttributeConstraintException.class,
        () -> sut.delete(new ExpenseAttributeIdentifier("attr-1")));
    assertEquals(1, attributeRepository.all().size());
  }
}
