package dev.yhiguchi.home_expense.infrastructure.fake;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.ConcurrentUpdateException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryIncomeAttributeRepository implements IncomeAttributeRepository {

  private static final long INITIAL_VERSION = 1L;

  private final Map<String, Revision<IncomeAttribute>> store = new LinkedHashMap<>();

  @Override
  public void register(IncomeAttribute incomeAttribute) {
    store.put(
        incomeAttribute.incomeAttributeIdentifier().value(),
        new Revision<>(incomeAttribute, INITIAL_VERSION));
  }

  @Override
  public void update(Revision<IncomeAttribute> versioned) {
    String key = versioned.entity().incomeAttributeIdentifier().value();
    Revision<IncomeAttribute> current = store.get(key);
    if (current == null || current.version() != versioned.version()) {
      throw new ConcurrentUpdateException();
    }
    store.put(key, new Revision<>(versioned.entity(), current.version() + 1));
  }

  @Override
  public void delete(IncomeAttribute incomeAttribute) {
    store.remove(incomeAttribute.incomeAttributeIdentifier().value());
  }

  @Override
  public Optional<Revision<IncomeAttribute>> findBy(
      IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return Optional.ofNullable(store.get(incomeAttributeIdentifier.value()));
  }

  @Override
  public boolean existsByName(IncomeAttributeName incomeAttributeName) {
    return store.values().stream()
        .anyMatch(v -> v.entity().incomeAttributeName().equals(incomeAttributeName));
  }

  public List<IncomeAttribute> all() {
    return store.values().stream().map(Revision::entity).toList();
  }
}
