package dev.yhiguchi.home_expense.domain.model.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** 経費一覧 */
public class Expenses implements Iterable<Expense> {
  List<Expense> values;

  public Expenses(List<Expense> values) {
    this.values = values;
  }

  public Expenses() {
    this.values = new ArrayList<>();
  }

  public boolean has(ExpenseAttribute expenseAttribute) {
    return values.stream().anyMatch(expense -> expense.expenseAttribute().equals(expenseAttribute));
  }

  @Override
  public Iterator<Expense> iterator() {
    return values.iterator();
  }
}
