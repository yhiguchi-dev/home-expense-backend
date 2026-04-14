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
  void 経費属性を更新する() {
    ExpenseAttribute existing =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費,
            1L);
    attributeRepository.register(existing);

    sut.update(
        new ExpenseAttributeIdentifier("attr-1"),
        new ExpenseAttributeName("交通費"),
        ExpenseCategory.変動費,
        1L);

    ExpenseAttribute updated = attributeRepository.get(new ExpenseAttributeIdentifier("attr-1"));
    assertEquals("交通費", updated.expenseAttributeName().value());
    assertEquals(1L, updated.version());
  }

  @Test
  void 変更がない場合は更新しない() {
    ExpenseAttribute existing =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費,
            1L);
    attributeRepository.register(existing);

    sut.update(
        new ExpenseAttributeIdentifier("attr-1"),
        new ExpenseAttributeName("食費"),
        ExpenseCategory.変動費,
        1L);

    ExpenseAttribute result = attributeRepository.get(new ExpenseAttributeIdentifier("attr-1"));
    assertSame(existing, result);
  }
}
