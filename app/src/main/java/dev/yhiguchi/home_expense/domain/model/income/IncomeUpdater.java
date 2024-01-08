package dev.yhiguchi.home_expense.domain.model.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import java.util.function.Consumer;
import java.util.function.Function;

/** 経費の更新者 */
public class IncomeUpdater {

  Function<IncomeIdentifier, Income> getFn;

  Function<IncomeAttributeIdentifier, IncomeAttribute> getAttributeFn;
  Consumer<Income> updateFn;

  public IncomeUpdater(
      Function<IncomeIdentifier, Income> getFn,
      Function<IncomeAttributeIdentifier, IncomeAttribute> getAttributeFn,
      Consumer<Income> updateFn) {
    this.getFn = getFn;
    this.getAttributeFn = getAttributeFn;
    this.updateFn = updateFn;
  }

  public void update(
      IncomeIdentifier incomeIdentifier,
      Description description,
      Amount amount,
      ReceiveDate receiveDate,
      IncomeAttributeIdentifier incomeAttributeIdentifier) {
    Income income = getFn.apply(incomeIdentifier);
    IncomeAttribute incomeAttribute = getAttributeFn.apply(incomeAttributeIdentifier);
    Income updated =
        new Income(incomeIdentifier, description, amount, receiveDate, incomeAttribute);
    if (income.equals(updated)) {
      return;
    }
    updateFn.accept(updated);
  }
}
