package dev.yhiguchi.home_expense.infrastructure.fake;

import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import java.util.*;
import java.util.stream.Collectors;

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
  public Income get(IncomeIdentifier incomeIdentifier) {
    Income income = store.get(incomeIdentifier.value());
    if (income == null) {
      throw new IncomeNotFoundException();
    }
    return income;
  }

  @Override
  public Incomes find(IncomeAttribute incomeAttribute) {
    List<Income> found =
        store.values().stream()
            .filter(i -> i.incomeAttribute().equals(incomeAttribute))
            .collect(Collectors.toList());
    return new Incomes(found);
  }

  public List<Income> all() {
    return List.copyOf(store.values());
  }
}
