package dev.yhiguchi.home_expense.application.usecase.income;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.infrastructure.fake.InMemoryIncomeRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class IncomeDeletionServiceTest {

  InMemoryIncomeRepository incomeRepository = new InMemoryIncomeRepository();
  IncomeDeletionService sut = new IncomeDeletionService(incomeRepository);

  @Test
  void 収入を削除する() {
    Income existing =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            new IncomeAttributeIdentifier("attr-1"),
            1L);
    incomeRepository.register(existing);

    sut.delete(new IncomeIdentifier("inc-1"));

    assertTrue(incomeRepository.all().isEmpty());
  }

  @Test
  void 存在しない収入を削除すると例外をスローする() {
    assertThrows(
        IncomeNotFoundException.class, () -> sut.delete(new IncomeIdentifier("not-exist")));
  }
}
