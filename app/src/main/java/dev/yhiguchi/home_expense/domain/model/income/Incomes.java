package dev.yhiguchi.home_expense.domain.model.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Incomes implements Iterable<Income> {
  List<Income> values;

  public Incomes(List<Income> values) {
    this.values = values;
  }

  public Incomes() {
    this(new ArrayList<>());
  }

  public boolean has(IncomeAttribute incomeAttribute) {
    return values.stream().anyMatch(income -> income.incomeAttribute().equals(incomeAttribute));
  }

  @Override
  public Iterator<Income> iterator() {
    return values.iterator();
  }
}
