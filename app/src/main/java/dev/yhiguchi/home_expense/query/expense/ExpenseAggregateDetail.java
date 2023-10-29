package dev.yhiguchi.home_expense.query.expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAggregateDetail {
  Integer totalAmount;
  List<ExpenseAttributeAggregate> list;

  public ExpenseAggregateDetail(Integer totalAmount, List<ExpenseAttributeAggregate> list) {
    this.totalAmount = totalAmount;
    this.list = list;
  }

  ExpenseAggregateDetail() {
    this(0, new ArrayList<>());
  }

  public Integer totalAmount() {
    return totalAmount;
  }

  public List<ExpenseAttributeAggregate> list() {
    return list;
  }
}
