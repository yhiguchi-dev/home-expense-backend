package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import java.util.ArrayList;
import java.util.List;

public class ExpenseSearchResult {

  Integer totalCount;

  List<Expense> list;

  public ExpenseSearchResult(Integer totalCount, List<Expense> list) {
    this.totalCount = totalCount;
    this.list = list;
  }

  public ExpenseSearchResult() {
    this(0, new ArrayList<>());
  }

  public Integer totalCount() {
    return totalCount;
  }

  public List<Expense> list() {
    return list;
  }
}
