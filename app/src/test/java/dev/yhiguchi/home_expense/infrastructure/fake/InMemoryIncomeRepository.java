package dev.yhiguchi.home_expense.infrastructure.fake;

import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import java.util.*;
import java.util.Optional;

public class InMemoryIncomeRepository implements IncomeRepository {

  private final Map<String, Income> store = new LinkedHashMap<>();

  @Override
  public void register(Income income) {
    store.put(income.incomeIdentifier().value(), income);
  }

  @Override
  public void update(Income income) {
    store.put(income.incomeIdentifier().value(), income);
  }

  @Override
  public void delete(Income income) {
    store.remove(income.incomeIdentifier().value());
  }

  @Override
  public Optional<Income> findBy(IncomeIdentifier incomeIdentifier) {
    return Optional.ofNullable(store.get(incomeIdentifier.value()));
  }

  @Override
  public boolean existsByAttributeIdentifier(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return store.values().stream()
        .anyMatch(i -> i.incomeAttributeIdentifier().equals(incomeAttributeIdentifier));
  }

  public List<Income> all() {
    return List.copyOf(store.values());
  }
}
