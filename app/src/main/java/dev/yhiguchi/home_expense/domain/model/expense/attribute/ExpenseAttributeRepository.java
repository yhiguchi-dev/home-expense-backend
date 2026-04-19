package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import java.util.Optional;

/** 経費属性リポジトリ */
public interface ExpenseAttributeRepository {
  void register(ExpenseAttribute expenseAttribute);

  Optional<ExpenseAttribute> findBy(ExpenseAttributeIdentifier expenseAttributeIdentifier);

  boolean existsByName(ExpenseAttributeName expenseAttributeName);

  void update(ExpenseAttribute expenseAttribute);

  void delete(ExpenseAttribute expenseAttribute);
}
