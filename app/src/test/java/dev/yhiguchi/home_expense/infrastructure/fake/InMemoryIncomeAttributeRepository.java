package dev.yhiguchi.home_expense.infrastructure.fake;

import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
  public Optional<IncomeAttribute> findBy(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return Optional.ofNullable(store.get(incomeAttributeIdentifier.value()));
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
