package dev.higuchi.homeexpense.command.service.income;

import dev.higuchi.homeexpense.command.model.income.Incomes;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeConstraintException;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeIdentifier;
import java.util.function.Consumer;
import java.util.function.Function;

/** 経費属性の削除者 */
public class IncomeAttributeDeleter {

  Function<IncomeAttributeIdentifier, IncomeAttribute> getFn;
  Consumer<IncomeAttributeIdentifier> deleteFn;

  Function<IncomeAttribute, Incomes> findIncomesFn;

  public IncomeAttributeDeleter(
      Function<IncomeAttributeIdentifier, IncomeAttribute> getFn,
      Consumer<IncomeAttributeIdentifier> deleteFn,
      Function<IncomeAttribute, Incomes> findIncomesFn) {
    this.getFn = getFn;
    this.deleteFn = deleteFn;
    this.findIncomesFn = findIncomesFn;
  }

  public void delete(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    IncomeAttribute expenseAttribute = getFn.apply(incomeAttributeIdentifier);
    Incomes expenses = findIncomesFn.apply(expenseAttribute);
    if (expenses.has(expenseAttribute)) {
      throw new IncomeAttributeConstraintException();
    }
    deleteFn.accept(expenseAttribute.incomeAttributeIdentifier());
  }
}
