package dev.yhiguchi.home_expense.domain.model.expense.attribute;

/** 経費属性リポジトリ */
public interface ExpenseAttributeRepository {
  void register(ExpenseAttribute expenseAttribute);

  ExpenseAttribute get(ExpenseAttributeIdentifier expenseAttributeIdentifier);

  boolean existsByName(ExpenseAttributeName expenseAttributeName);

  void update(ExpenseAttribute expenseAttribute);

  void delete(ExpenseAttributeIdentifier expenseAttributeIdentifier);
}
