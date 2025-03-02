package dev.higuchi.homeexpense.command.model.expense;

import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;

/** 経費リポジトリ */
public interface ExpenseRepository {

  void register(Expense expense);

  Expense get(ExpenseIdentifier expenseIdentifier);

  Expense find(ExpenseIdentifier expenseIdentifier);

  Expenses find(ExpenseAttribute expenseAttribute);

  void update(Expense expense);

  void delete(ExpenseIdentifier expenseIdentifier);
}
