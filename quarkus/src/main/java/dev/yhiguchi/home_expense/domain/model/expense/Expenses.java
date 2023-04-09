package dev.yhiguchi.home_expense.domain.model.expense;

import java.util.Iterator;
import java.util.List;

/** 経費一覧 */
public class Expenses implements Iterable<Expense> {
  List<Expense> values;

  public Expenses(List<Expense> values) {
    this.values = values;
  }

  @Override
  public Iterator<Expense> iterator() {
    return values.iterator();
  }
}
