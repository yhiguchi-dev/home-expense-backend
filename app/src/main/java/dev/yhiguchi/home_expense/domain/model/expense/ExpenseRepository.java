package dev.yhiguchi.home_expense.domain.model.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import java.util.Optional;

/** 経費リポジトリ */
public interface ExpenseRepository {

  void register(Expense expense);

  Optional<Expense> findBy(ExpenseIdentifier expenseIdentifier);

  boolean existsByAttributeIdentifier(ExpenseAttributeIdentifier expenseAttributeIdentifier);

  void update(Expense expense);

  void delete(Expense expense);
}
