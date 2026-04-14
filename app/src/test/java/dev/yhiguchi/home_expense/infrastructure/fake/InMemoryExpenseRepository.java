package dev.yhiguchi.home_expense.infrastructure.fake;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryExpenseRepository implements ExpenseRepository {

  private final Map<String, Expense> store = new LinkedHashMap<>();

  @Override
  public void register(Expense expense) {
    store.put(expense.expenseIdentifier().value(), expense);
  }

  @Override
  public Expense get(ExpenseIdentifier expenseIdentifier) {
    Expense expense = store.get(expenseIdentifier.value());
    if (expense == null) {
      throw new ExpenseNotFoundException();
    }
    return expense;
  }

  @Override
  public Expenses find(ExpenseAttribute expenseAttribute) {
    List<Expense> found =
        store.values().stream()
            .filter(e -> e.expenseAttribute().equals(expenseAttribute))
            .collect(Collectors.toList());
    return new Expenses(found);
  }

  @Override
  public void update(Expense expense) {
    store.put(expense.expenseIdentifier().value(), expense);
  }

  @Override
  public void delete(ExpenseIdentifier expenseIdentifier) {
    store.remove(expenseIdentifier.value());
  }

  public List<Expense> all() {
    return List.copyOf(store.values());
  }
}
