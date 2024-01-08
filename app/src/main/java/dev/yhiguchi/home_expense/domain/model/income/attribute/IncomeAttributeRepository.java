package dev.yhiguchi.home_expense.domain.model.income.attribute;

/** 収入属性リポジトリ */
public interface IncomeAttributeRepository {
  void register(IncomeAttribute incomeAttribute);

  void update(IncomeAttribute incomeAttribute);

  void delete(IncomeAttributeIdentifier incomeAttributeIdentifier);

  IncomeAttribute get(IncomeAttributeIdentifier incomeAttributeIdentifier);

  IncomeAttribute find(IncomeAttributeName incomeAttributeName);
}
