package dev.yhiguchi.home_expense.infrastructure.fake;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.infrastructure.datasource.ConcurrentUpdateException;
import java.util.*;
import java.util.Optional;

public class InMemoryIncomeRepository implements IncomeRepository {

  private static final long INITIAL_VERSION = 1L;

  private final Map<String, Revision<Income>> store = new LinkedHashMap<>();

  @Override
  public void register(Income income) {
    store.put(income.incomeIdentifier().value(), new Revision<>(income, INITIAL_VERSION));
  }

  @Override
  public void update(Revision<Income> versioned) {
    String key = versioned.entity().incomeIdentifier().value();
    Revision<Income> current = store.get(key);
    if (current == null || current.version() != versioned.version()) {
      throw new ConcurrentUpdateException();
    }
    store.put(key, new Revision<>(versioned.entity(), current.version() + 1));
  }

  @Override
  public void delete(Income income) {
    store.remove(income.incomeIdentifier().value());
  }

  @Override
  public Optional<Revision<Income>> findBy(IncomeIdentifier incomeIdentifier) {
    return Optional.ofNullable(store.get(incomeIdentifier.value()));
  }

  @Override
  public boolean existsByAttributeIdentifier(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return store.values().stream()
        .anyMatch(v -> v.entity().incomeAttributeIdentifier().equals(incomeAttributeIdentifier));
  }

  public List<Income> all() {
    return store.values().stream().map(Revision::entity).toList();
  }
}
