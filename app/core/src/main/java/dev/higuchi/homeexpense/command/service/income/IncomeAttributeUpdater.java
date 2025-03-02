package dev.higuchi.homeexpense.command.service.income;

import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeIdentifier;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeName;
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
