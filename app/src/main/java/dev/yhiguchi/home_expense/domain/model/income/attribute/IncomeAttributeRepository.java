package dev.yhiguchi.home_expense.domain.model.income.attribute;

import dev.yhiguchi.home_expense.domain.model.Revision;
import java.util.Optional;

/** 収入属性リポジトリ */
public interface IncomeAttributeRepository {
  void register(IncomeAttribute incomeAttribute);

  void update(Revision<IncomeAttribute> incomeAttribute);

  void delete(IncomeAttribute incomeAttribute);

  Optional<Revision<IncomeAttribute>> findBy(IncomeAttributeIdentifier incomeAttributeIdentifier);

  boolean existsByName(IncomeAttributeName incomeAttributeName);
}
