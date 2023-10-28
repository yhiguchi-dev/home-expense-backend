package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import java.util.ArrayList;
import java.util.List;

public class ExpenseAggregateDetail {
  Integer totalAmount;
  List<Expense> list;

  public ExpenseAggregateDetail(Integer totalAmount, List<Expense> list) {
    this.totalAmount = totalAmount;
    this.list = list;
  }

  ExpenseAggregateDetail() {
    this(0, new ArrayList<>());
  }

  public Integer totalAmount() {
    return totalAmount;
  }

  public List<Expense> list() {
    return list;
  }
}
