package dev.yhiguchi.home_expense.domain.model.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import java.util.Optional;

/** 経費リポジトリ */
public interface ExpenseRepository {

  void register(Expense expense);

  Expense get(ExpenseIdentifier expenseIdentifier);

  Optional<Expense> find(ExpenseIdentifier expenseIdentifier);

  Expenses find(ExpenseAttribute expenseAttribute);

  void update(Expense expense);

  void delete(ExpenseIdentifier expenseIdentifier);
}
