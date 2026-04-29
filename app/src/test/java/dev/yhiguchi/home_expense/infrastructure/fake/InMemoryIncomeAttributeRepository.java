package dev.yhiguchi.home_expense.infrastructure.fake;

import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.ConcurrentUpdateException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryIncomeAttributeRepository implements IncomeAttributeRepository {

  private static final long INITIAL_VERSION = 1L;

  private record Stored(IncomeAttribute entity, long version) {}

  private final Map<String, Stored> store = new LinkedHashMap<>();

  @Override
  public void register(IncomeAttribute incomeAttribute) {
    store.put(
        incomeAttribute.incomeAttributeIdentifier().value(),
        new Stored(incomeAttribute, INITIAL_VERSION));
  }

  @Override
  public void update(IncomeAttribute incomeAttribute, long expectedVersion) {
    String key = incomeAttribute.incomeAttributeIdentifier().value();
    Stored current = store.get(key);
    if (current == null || current.version() != expectedVersion) {
      throw new ConcurrentUpdateException();
    }
    store.put(key, new Stored(incomeAttribute, current.version() + 1));
  }

  @Override
  public void delete(IncomeAttribute incomeAttribute) {
    store.remove(incomeAttribute.incomeAttributeIdentifier().value());
  }

  @Override
  public Optional<IncomeAttribute> find(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return Optional.ofNullable(store.get(incomeAttributeIdentifier.value())).map(Stored::entity);
  }

  @Override
  public boolean existsByName(IncomeAttributeName incomeAttributeName) {
    return store.values().stream()
        .anyMatch(v -> v.entity().incomeAttributeName().equals(incomeAttributeName));
  }

  public List<IncomeAttribute> all() {
    return store.values().stream().map(Stored::entity).toList();
  }

  /** テスト用: 現在保持している version を取得 */
  public long versionOf(IncomeAttributeIdentifier id) {
    Stored s = store.get(id.value());
    if (s == null) {
      throw new IllegalStateException("not found: " + id.value());
    }
    return s.version();
  }
}
