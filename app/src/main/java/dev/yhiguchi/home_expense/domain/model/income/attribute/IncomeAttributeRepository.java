package dev.yhiguchi.home_expense.domain.model.income.attribute;

import java.util.Optional;

/** 収入属性リポジトリ */
public interface IncomeAttributeRepository {
  void register(IncomeAttribute incomeAttribute);

  void update(IncomeAttribute incomeAttribute);

  void delete(IncomeAttribute incomeAttribute);

  Optional<IncomeAttribute> findBy(IncomeAttributeIdentifier incomeAttributeIdentifier);

  boolean existsByName(IncomeAttributeName incomeAttributeName);
}
