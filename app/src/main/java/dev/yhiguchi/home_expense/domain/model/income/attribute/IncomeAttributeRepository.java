package dev.yhiguchi.home_expense.domain.model.income.attribute;

import java.util.Optional;

/** 収入属性リポジトリ */
public interface IncomeAttributeRepository {

  void register(IncomeAttribute incomeAttribute);

  Optional<IncomeAttribute> find(IncomeAttributeIdentifier incomeAttributeIdentifier);

  default IncomeAttribute get(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return find(incomeAttributeIdentifier).orElseThrow(IncomeAttributeNotFoundException::new);
  }

  boolean existsByName(IncomeAttributeName incomeAttributeName);

  void update(IncomeAttribute incomeAttribute, long expectedVersion);

  void delete(IncomeAttribute incomeAttribute);
}
