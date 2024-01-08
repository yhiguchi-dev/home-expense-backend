package dev.yhiguchi.home_expense.domain.model.income.attribute;

import java.util.function.Consumer;
import java.util.function.Function;

/** 収入属性の更新者 */
public class IncomeAttributeUpdater {

  Function<IncomeAttributeIdentifier, IncomeAttribute> getFn;
  Consumer<IncomeAttribute> updateFn;

  public IncomeAttributeUpdater(
      Function<IncomeAttributeIdentifier, IncomeAttribute> getFn,
      Consumer<IncomeAttribute> updateFn) {
    this.getFn = getFn;
    this.updateFn = updateFn;
  }

  public void update(
      IncomeAttributeIdentifier incomeAttributeIdentifier,
      IncomeAttributeName incomeAttributeName) {
    IncomeAttribute incomeAttribute = getFn.apply(incomeAttributeIdentifier);
    IncomeAttribute updated = new IncomeAttribute(incomeAttributeIdentifier, incomeAttributeName);
    if (incomeAttribute.equals(updated)) {
      return;
    }
    updateFn.accept(updated);
  }
}
