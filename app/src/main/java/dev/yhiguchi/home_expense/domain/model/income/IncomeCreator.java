package dev.yhiguchi.home_expense.domain.model.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

/** 収入作成者 */
public class IncomeCreator {

  Function<IncomeAttributeIdentifier, IncomeAttribute> getFn;
  Consumer<Income> registerFn;

  public IncomeCreator(
      Function<IncomeAttributeIdentifier, IncomeAttribute> getFn, Consumer<Income> registerFn) {
    this.getFn = getFn;
    this.registerFn = registerFn;
  }

  public Income create(
      Description description,
      Amount amount,
      ReceiveDate receiveDate,
      IncomeAttributeIdentifier incomeAttributeIdentifier) {
    IncomeIdentifier incomeIdentifier = new IncomeIdentifier(UUID.randomUUID().toString());
    IncomeAttribute incomeAttribute = getFn.apply(incomeAttributeIdentifier);
    Income income = new Income(incomeIdentifier, description, amount, receiveDate, incomeAttribute);
    registerFn.accept(income);
    return income;
  }
}
