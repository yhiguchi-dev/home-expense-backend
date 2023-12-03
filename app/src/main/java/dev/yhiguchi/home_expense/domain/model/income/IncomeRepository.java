package dev.yhiguchi.home_expense.domain.model.income;

public interface IncomeRepository {

  void register(Income income);

  void update(Income income);

  void delete(IncomeIdentifier incomeIdentifier);

  Income get(IncomeIdentifier incomeIdentifier);
}
