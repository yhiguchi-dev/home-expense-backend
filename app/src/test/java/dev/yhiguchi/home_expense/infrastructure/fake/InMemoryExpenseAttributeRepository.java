package dev.yhiguchi.home_expense.infrastructure.fake;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryExpenseAttributeRepository implements ExpenseAttributeRepository {

  private final Map<String, ExpenseAttribute> store = new LinkedHashMap<>();

  @Override
  public void register(ExpenseAttribute expenseAttribute) {
    store.put(expenseAttribute.expenseAttributeIdentifier().value(), expenseAttribute);
  }

  @Override
  public Optional<ExpenseAttribute> findBy(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    return Optional.ofNullable(store.get(expenseAttributeIdentifier.value()));
  }

  @Override
  public boolean existsByName(ExpenseAttributeName expenseAttributeName) {
    return store.values().stream()
        .anyMatch(a -> a.expenseAttributeName().equals(expenseAttributeName));
  }

  @Override
  public void update(ExpenseAttribute expenseAttribute) {
    store.put(expenseAttribute.expenseAttributeIdentifier().value(), expenseAttribute);
  }

  @Override
  public void delete(ExpenseAttribute expenseAttribute) {
    store.remove(expenseAttribute.expenseAttributeIdentifier().value());
  }

  public List<ExpenseAttribute> all() {
    return List.copyOf(store.values());
  }
}
