package dev.yhiguchi.home_expense.domain.model.income.attribute;

import java.util.Optional;

/** 収入属性リポジトリ */
public interface IncomeAttributeRepository extends IncomeAttributeNameLookup {

  void register(IncomeAttribute incomeAttribute);

  Optional<IncomeAttribute> find(IncomeAttributeIdentifier incomeAttributeIdentifier);

  default IncomeAttribute get(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return find(incomeAttributeIdentifier).orElseThrow(IncomeAttributeNotFoundException::new);
  }

  void update(IncomeAttribute incomeAttribute, long expectedVersion);

  void delete(IncomeAttribute incomeAttribute);
}
