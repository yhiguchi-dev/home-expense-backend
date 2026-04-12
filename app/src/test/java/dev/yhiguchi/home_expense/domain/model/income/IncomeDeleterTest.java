package dev.yhiguchi.home_expense.domain.model.income;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeName;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class IncomeDeleterTest {

  @Test
  void 収入を削除する() {
    Income existing =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            new IncomeAttribute(
                new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与")),
            1L);

    List<IncomeIdentifier> deleted = new ArrayList<>();

    IncomeDeleter deleter = new IncomeDeleter(id -> existing, deleted::add);
    deleter.delete(new IncomeIdentifier("inc-1"));

    assertEquals(1, deleted.size());
    assertEquals("inc-1", deleted.getFirst().value());
  }
}
