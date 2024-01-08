package dev.yhiguchi.home_expense.domain.model.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;

/** 収入リポジトリ */
public interface IncomeRepository {

  void register(Income income);

  void update(Income income);

  void delete(IncomeIdentifier incomeIdentifier);

  Income get(IncomeIdentifier incomeIdentifier);

  Incomes find(IncomeAttribute incomeAttribute);
}
