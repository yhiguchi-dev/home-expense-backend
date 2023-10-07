package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import java.util.ArrayList;
import java.util.List;

public class ExpenseSummary {

  Integer totalNumber;

  List<Expense> list;

  public ExpenseSummary(Integer totalNumber, List<Expense> list) {
    this.totalNumber = totalNumber;
    this.list = list;
  }

  public ExpenseSummary() {
    this(0, new ArrayList<>());
  }

  public Integer totalNumber() {
    return totalNumber;
  }

  public List<Expense> list() {
    return list;
  }
}
