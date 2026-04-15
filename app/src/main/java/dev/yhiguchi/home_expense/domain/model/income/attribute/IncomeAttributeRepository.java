package dev.yhiguchi.home_expense.domain.model.income.attribute;

/** 収入属性リポジトリ */
public interface IncomeAttributeRepository {
  void register(IncomeAttribute incomeAttribute);

  void update(IncomeAttribute incomeAttribute);

  void delete(IncomeAttribute incomeAttribute);

  IncomeAttribute get(IncomeAttributeIdentifier incomeAttributeIdentifier);

  boolean existsByName(IncomeAttributeName incomeAttributeName);
}
