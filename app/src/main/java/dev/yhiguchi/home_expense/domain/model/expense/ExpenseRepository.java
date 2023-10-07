package dev.yhiguchi.home_expense.domain.model.expense;

/** 経費リポジトリ */
public interface ExpenseRepository {

  void register(Expense expense);

  Expense get(ExpenseIdentifier expenseIdentifier);

  Expense find(ExpenseIdentifier expenseIdentifier);

  void update(Expense expense);

  void delete(ExpenseIdentifier expenseIdentifier);
}
