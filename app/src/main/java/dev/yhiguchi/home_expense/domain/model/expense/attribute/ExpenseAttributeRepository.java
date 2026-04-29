package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import java.util.Optional;

/** 経費属性リポジトリ */
public interface ExpenseAttributeRepository {

  void register(ExpenseAttribute expenseAttribute);

  Optional<ExpenseAttribute> find(ExpenseAttributeIdentifier expenseAttributeIdentifier);

  default ExpenseAttribute get(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    return find(expenseAttributeIdentifier).orElseThrow(ExpenseAttributeNotFoundException::new);
  }

  boolean existsByName(ExpenseAttributeName expenseAttributeName, ExpenseCategory expenseCategory);

  void update(ExpenseAttribute expenseAttribute, long expectedVersion);

  void delete(ExpenseAttribute expenseAttribute);
}
