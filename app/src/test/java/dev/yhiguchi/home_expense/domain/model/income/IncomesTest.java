package dev.yhiguchi.home_expense.domain.model.income;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeName;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class IncomesTest {

  IncomeAttribute attribute =
      new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));

  @Test
  void 一致する属性を持つ収入がある場合trueを返す() {
    Income income =
        new Income(
            new IncomeIdentifier("inc-1"),
            new Description("4月給与"),
            new Amount(300000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute);
    Incomes incomes = new Incomes(List.of(income));

    assertTrue(incomes.has(attribute));
  }

  @Test
  void 空の場合falseを返す() {
    Incomes incomes = new Incomes();

    assertFalse(incomes.has(attribute));
  }
}
