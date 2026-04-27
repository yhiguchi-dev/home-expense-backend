package dev.yhiguchi.home_expense.infrastructure.fake;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.infrastructure.datasource.ConcurrentUpdateException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryExpenseRepository implements ExpenseRepository {

  private static final long INITIAL_VERSION = 1L;

  private final Map<String, Revision<Expense>> store = new LinkedHashMap<>();

  @Override
  public void register(Expense expense) {
    store.put(expense.expenseIdentifier().value(), new Revision<>(expense, INITIAL_VERSION));
  }

  @Override
  public Optional<Revision<Expense>> findBy(ExpenseIdentifier expenseIdentifier) {
    return Optional.ofNullable(store.get(expenseIdentifier.value()));
  }

  @Override
  public boolean existsByAttributeIdentifier(
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    return store.values().stream()
        .anyMatch(v -> v.entity().expenseAttributeIdentifier().equals(expenseAttributeIdentifier));
  }

  @Override
  public void update(Revision<Expense> versioned) {
    String key = versioned.entity().expenseIdentifier().value();
    Revision<Expense> current = store.get(key);
    if (current == null || current.version() != versioned.version()) {
      throw new ConcurrentUpdateException();
    }
    store.put(key, new Revision<>(versioned.entity(), current.version() + 1));
  }

  @Override
  public void delete(Expense expense) {
    store.remove(expense.expenseIdentifier().value());
  }

  public List<Expense> all() {
    return store.values().stream().map(Revision::entity).toList();
  }
}
