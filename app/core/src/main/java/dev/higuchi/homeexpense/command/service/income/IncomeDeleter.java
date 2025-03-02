package dev.higuchi.homeexpense.command.service.income;

import dev.higuchi.homeexpense.command.model.income.Income;
import dev.higuchi.homeexpense.command.model.income.IncomeIdentifier;
import java.util.function.Consumer;
import java.util.function.Function;

/** 経費の削除者 */
public class IncomeDeleter {

  Function<IncomeIdentifier, Income> getFn;
  Consumer<IncomeIdentifier> deleteFn;

  public IncomeDeleter(
      Function<IncomeIdentifier, Income> getFn, Consumer<IncomeIdentifier> deleteFn) {
    this.getFn = getFn;
    this.deleteFn = deleteFn;
  }

  public void delete(IncomeIdentifier incomeIdentifier) {
    Income income = getFn.apply(incomeIdentifier);
    deleteFn.accept(income.incomeIdentifier());
  }
}
