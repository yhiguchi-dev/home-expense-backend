package dev.yhiguchi.home_expense.domain.model.income.attribute;

public interface IncomeAttributeRepository {
  void register(IncomeAttribute incomeAttribute);

  void update(IncomeAttribute incomeAttribute);

  void delete(IncomeAttributeIdentifier incomeAttributeIdentifier);

  IncomeAttribute get(IncomeAttributeIdentifier incomeAttributeIdentifier);
}
