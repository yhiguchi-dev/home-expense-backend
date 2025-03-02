package dev.higuchi.homeexpense.query.model.expense;

import dev.higuchi.homeexpense.command.model.expense.Expense;
import java.util.ArrayList;
import java.util.List;

public class ExpenseSummary {

  Integer totalCount;

  List<Expense> list;

  public ExpenseSummary(Integer totalCount, List<Expense> list) {
    this.totalCount = totalCount;
    this.list = list;
  }

  public ExpenseSummary() {
    this(0, new ArrayList<>());
  }

  public Integer totalCount() {
    return totalCount;
  }

  public List<Expense> list() {
    return list;
  }
}
