package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import java.util.Optional;

/** 経費属性リポジトリ */
public interface ExpenseAttributeRepository extends ExpenseAttributeNameLookup {

  void register(ExpenseAttribute expenseAttribute);

  Optional<ExpenseAttribute> find(ExpenseAttributeIdentifier expenseAttributeIdentifier);

  default ExpenseAttribute get(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    return find(expenseAttributeIdentifier).orElseThrow(ExpenseAttributeNotFoundException::new);
  }

  void update(ExpenseAttribute expenseAttribute, long expectedVersion);

  void delete(ExpenseAttribute expenseAttribute);
}
