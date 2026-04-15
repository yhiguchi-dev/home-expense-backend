package dev.yhiguchi.home_expense.domain.model.income.attribute;

import dev.yhiguchi.home_expense.domain.model.income.Incomes;

/** 収入属性の削除可否を判定する */
public class IncomeAttributeDeletionPolicy {

  @FunctionalInterface
  public interface IncomeFinder {
    Incomes findBy(IncomeAttribute incomeAttribute);
  }

  IncomeFinder incomeFinder;

  public IncomeAttributeDeletionPolicy(IncomeFinder incomeFinder) {
    this.incomeFinder = incomeFinder;
  }

  public void assertDeletable(IncomeAttribute incomeAttribute) {
    Incomes incomes = incomeFinder.findBy(incomeAttribute);
    if (incomes.has(incomeAttribute)) {
      throw new IncomeAttributeConstraintException();
    }
  }
}
