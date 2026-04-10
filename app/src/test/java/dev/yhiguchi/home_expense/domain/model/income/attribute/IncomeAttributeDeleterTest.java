package dev.yhiguchi.home_expense.domain.model.income.attribute;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.income.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class IncomeAttributeDeleterTest {

  IncomeAttribute attribute =
      new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));

  @Test
  void 収入属性を削除する() {
    List<IncomeAttributeIdentifier> deleted = new ArrayList<>();
    Incomes emptyIncomes = new Incomes();

    IncomeAttributeDeleter deleter =
        new IncomeAttributeDeleter(id -> attribute, deleted::add, attr -> emptyIncomes);

    deleter.delete(new IncomeAttributeIdentifier("attr-1"));

    assertEquals(1, deleted.size());
    assertEquals("attr-1", deleted.getFirst().value());
  }

  @Test
  void 参照する収入がある場合は例外をスローする() {
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute);
    Incomes incomesWithAttribute = new Incomes(List.of(income));

    IncomeAttributeDeleter deleter =
        new IncomeAttributeDeleter(id -> attribute, id -> {}, attr -> incomesWithAttribute);

    assertThrows(
        IncomeAttributeConstraintException.class,
        () -> deleter.delete(new IncomeAttributeIdentifier("attr-1")));
  }
}
