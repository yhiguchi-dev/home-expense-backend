package dev.yhiguchi.home_expense.infrastructure.fake;

import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryIncomeAttributeRepository implements IncomeAttributeRepository {

  private final Map<String, IncomeAttribute> store = new LinkedHashMap<>();

  @Override
  public void register(IncomeAttribute incomeAttribute) {
    store.put(incomeAttribute.incomeAttributeIdentifier().value(), incomeAttribute);
  }

  @Override
  public void update(IncomeAttribute incomeAttribute) {
    store.put(incomeAttribute.incomeAttributeIdentifier().value(), incomeAttribute);
  }

  @Override
  public void delete(IncomeAttribute incomeAttribute) {
    store.remove(incomeAttribute.incomeAttributeIdentifier().value());
  }

  @Override
  public IncomeAttribute get(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    IncomeAttribute attribute = store.get(incomeAttributeIdentifier.value());
    if (attribute == null) {
      throw new IncomeAttributeNotFoundException();
    }
    return attribute;
  }

  @Override
  public boolean existsByName(IncomeAttributeName incomeAttributeName) {
    return store.values().stream()
        .anyMatch(a -> a.incomeAttributeName().equals(incomeAttributeName));
  }

  public List<IncomeAttribute> all() {
    return List.copyOf(store.values());
  }
}
