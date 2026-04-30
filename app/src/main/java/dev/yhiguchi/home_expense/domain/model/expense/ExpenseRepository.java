package dev.yhiguchi.home_expense.domain.model.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import java.util.Optional;

/** 経費リポジトリ */
public interface ExpenseRepository {

  void register(Expense expense);

  Optional<Expense> find(ExpenseIdentifier expenseIdentifier);

  default Expense get(ExpenseIdentifier expenseIdentifier) {
    return find(expenseIdentifier).orElseThrow(ExpenseNotFoundException::new);
  }

  boolean existsByAttributeIdentifier(ExpenseAttributeIdentifier expenseAttributeIdentifier);

  void update(Expense expense, long expectedVersion);

  void delete(Expense expense);
}
