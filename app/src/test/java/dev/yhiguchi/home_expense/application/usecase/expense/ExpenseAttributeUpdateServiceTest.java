package dev.yhiguchi.home_expense.application.usecase.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryExpenseAttributeRepository;
import org.junit.jupiter.api.Test;

class ExpenseAttributeUpdateServiceTest {

  InMemoryExpenseAttributeRepository attributeRepository = new InMemoryExpenseAttributeRepository();
  ExpenseAttributeUpdateService sut = new ExpenseAttributeUpdateService(attributeRepository);

  @Test
  void 経費属性の名前を更新する() {
    ExpenseAttribute existing =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費);
    attributeRepository.register(existing);

    sut.update(
        new ExpenseAttributeUpdateCommand(
            new ExpenseAttributeIdentifier("attr-1"), new ExpenseAttributeName("交通費"), 1L));

    ExpenseAttribute updated =
        attributeRepository.find(new ExpenseAttributeIdentifier("attr-1")).orElseThrow();
    assertEquals("交通費", updated.expenseAttributeName().value());
    assertEquals(ExpenseCategory.変動費, updated.expenseCategory());
    assertEquals(2L, attributeRepository.versionOf(new ExpenseAttributeIdentifier("attr-1")));
  }

  @Test
  void 名前に変更がない場合は更新しない() {
    ExpenseAttribute existing =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費);
    attributeRepository.register(existing);

    sut.update(
        new ExpenseAttributeUpdateCommand(
            new ExpenseAttributeIdentifier("attr-1"), new ExpenseAttributeName("食費"), 1L));

    ExpenseAttribute result =
        attributeRepository.find(new ExpenseAttributeIdentifier("attr-1")).orElseThrow();
    assertSame(existing, result);
    assertEquals(1L, attributeRepository.versionOf(new ExpenseAttributeIdentifier("attr-1")));
  }
}
