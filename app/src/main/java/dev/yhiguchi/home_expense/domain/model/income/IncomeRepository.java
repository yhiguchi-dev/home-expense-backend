package dev.yhiguchi.home_expense.domain.model.income;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import java.util.Optional;

/** 収入リポジトリ */
public interface IncomeRepository {

  void register(Income income);

  void update(Revision<Income> income);

  void delete(Income income);

  Optional<Revision<Income>> findBy(IncomeIdentifier incomeIdentifier);

  boolean existsByAttributeIdentifier(IncomeAttributeIdentifier incomeAttributeIdentifier);
}
