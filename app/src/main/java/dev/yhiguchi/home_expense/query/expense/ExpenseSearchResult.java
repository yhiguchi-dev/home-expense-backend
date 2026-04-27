package dev.yhiguchi.home_expense.query.expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseSearchResult {

  Integer totalCount;

  List<ExpenseDetail> list;

  public ExpenseSearchResult(Integer totalCount, List<ExpenseDetail> list) {
    this.totalCount = totalCount;
    this.list = list;
  }

  public ExpenseSearchResult() {
    this(0, new ArrayList<>());
  }

  public Integer totalCount() {
    return totalCount;
  }

  public List<ExpenseDetail> list() {
    return list;
  }
}
