package dev.yhiguchi.home_expense.infrastructure.fake;

import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.infrastructure.datasource.ConcurrentUpdateException;
import java.util.*;
import java.util.Optional;

public class InMemoryIncomeRepository implements IncomeRepository {

  private static final long INITIAL_VERSION = 1L;

  private record Stored(Income entity, long version) {}

  private final Map<String, Stored> store = new LinkedHashMap<>();

  @Override
  public void register(Income income) {
    store.put(income.incomeIdentifier().value(), new Stored(income, INITIAL_VERSION));
  }

  @Override
  public void update(Income income, long expectedVersion) {
    String key = income.incomeIdentifier().value();
    Stored current = store.get(key);
    if (current == null || current.version() != expectedVersion) {
      throw new ConcurrentUpdateException();
    }
    store.put(key, new Stored(income, current.version() + 1));
  }

  @Override
  public void delete(Income income) {
    store.remove(income.incomeIdentifier().value());
  }

  @Override
  public Optional<Income> find(IncomeIdentifier incomeIdentifier) {
    return Optional.ofNullable(store.get(incomeIdentifier.value())).map(Stored::entity);
  }

  @Override
  public boolean existsByAttributeIdentifier(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return store.values().stream()
        .anyMatch(v -> v.entity().incomeAttributeIdentifier().equals(incomeAttributeIdentifier));
  }

  public List<Income> all() {
    return store.values().stream().map(Stored::entity).toList();
  }

  /** テスト用: 現在保持している version を取得 */
  public long versionOf(IncomeIdentifier id) {
    Stored s = store.get(id.value());
    if (s == null) {
      throw new IllegalStateException("not found: " + id.value());
    }
    return s.version();
  }
}
