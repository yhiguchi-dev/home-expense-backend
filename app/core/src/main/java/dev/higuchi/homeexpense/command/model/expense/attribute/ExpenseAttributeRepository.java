package dev.higuchi.homeexpense.command.model.expense.attribute;

/** 経費属性リポジトリ */
public interface ExpenseAttributeRepository {
  void register(ExpenseAttribute expenseAttribute);

  ExpenseAttribute get(ExpenseAttributeIdentifier expenseAttributeIdentifier);

  ExpenseAttribute find(ExpenseAttributeName expenseAttributeName);

  void update(ExpenseAttribute expenseAttribute);

  void delete(ExpenseAttributeIdentifier expenseAttributeIdentifier);
}
