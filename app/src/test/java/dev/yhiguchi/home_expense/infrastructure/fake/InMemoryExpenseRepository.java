package dev.yhiguchi.home_expense.infrastructure.fake;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryExpenseRepository implements ExpenseRepository {

  private final Map<String, Expense> store = new LinkedHashMap<>();

  @Override
  public void register(Expense expense) {
    store.put(expense.expenseIdentifier().value(), expense);
  }

  @Override
  public Optional<Expense> findBy(ExpenseIdentifier expenseIdentifier) {
    return Optional.ofNullable(store.get(expenseIdentifier.value()));
  }

  @Override
  public boolean existsByAttributeIdentifier(
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    return store.values().stream()
        .anyMatch(e -> e.expenseAttributeIdentifier().equals(expenseAttributeIdentifier));
  }

  @Override
  public void update(Expense expense) {
    store.put(expense.expenseIdentifier().value(), expense);
  }

  @Override
  public void delete(Expense expense) {
    store.remove(expense.expenseIdentifier().value());
  }

  public List<Expense> all() {
    return List.copyOf(store.values());
  }
}
