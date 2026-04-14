package dev.yhiguchi.home_expense.infrastructure.fake;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryExpenseAttributeRepository implements ExpenseAttributeRepository {

  private final Map<String, ExpenseAttribute> store = new LinkedHashMap<>();

  @Override
  public void register(ExpenseAttribute expenseAttribute) {
    store.put(expenseAttribute.expenseAttributeIdentifier().value(), expenseAttribute);
  }

  @Override
  public ExpenseAttribute get(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    ExpenseAttribute attribute = store.get(expenseAttributeIdentifier.value());
    if (attribute == null) {
      throw new ExpenseAttributeNotFoundException();
    }
    return attribute;
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
  public void delete(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    store.remove(expenseAttributeIdentifier.value());
  }

  public List<ExpenseAttribute> all() {
    return List.copyOf(store.values());
  }
}
