package dev.yhiguchi.home_expense.infrastructure.fake;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.ConcurrentUpdateException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryExpenseAttributeRepository implements ExpenseAttributeRepository {

  private static final long INITIAL_VERSION = 1L;

  private final Map<String, Revision<ExpenseAttribute>> store = new LinkedHashMap<>();

  @Override
  public void register(ExpenseAttribute expenseAttribute) {
    store.put(
        expenseAttribute.expenseAttributeIdentifier().value(),
        new Revision<>(expenseAttribute, INITIAL_VERSION));
  }

  @Override
  public Optional<Revision<ExpenseAttribute>> findBy(
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    return Optional.ofNullable(store.get(expenseAttributeIdentifier.value()));
  }

  @Override
  public boolean existsByName(
      ExpenseAttributeName expenseAttributeName, ExpenseCategory expenseCategory) {
    return store.values().stream()
        .anyMatch(
            v ->
                v.entity().expenseAttributeName().equals(expenseAttributeName)
                    && v.entity().expenseCategory() == expenseCategory);
  }

  @Override
  public void update(Revision<ExpenseAttribute> versioned) {
    String key = versioned.entity().expenseAttributeIdentifier().value();
    Revision<ExpenseAttribute> current = store.get(key);
    if (current == null || current.version() != versioned.version()) {
      throw new ConcurrentUpdateException();
    }
    store.put(key, new Revision<>(versioned.entity(), current.version() + 1));
  }

  @Override
  public void delete(ExpenseAttribute expenseAttribute) {
    store.remove(expenseAttribute.expenseAttributeIdentifier().value());
  }

  public List<ExpenseAttribute> all() {
    return store.values().stream().map(Revision::entity).toList();
  }
}
