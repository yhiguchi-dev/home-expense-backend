package dev.yhiguchi.home_expense.query.expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseStatisticsDetail {
  long totalAmount;
  List<ExpenseAttributeStatistics> list;

  public ExpenseStatisticsDetail(long totalAmount, List<ExpenseAttributeStatistics> list) {
    this.totalAmount = totalAmount;
    this.list = list;
  }

  public ExpenseStatisticsDetail(List<ExpenseAttributeStatistics> list) {
    this.totalAmount = list.stream().mapToLong(ExpenseAttributeStatistics::totalAmount).sum();
    this.list = list;
  }

  ExpenseStatisticsDetail() {
    this(0L, new ArrayList<>());
  }

  public long totalAmount() {
    return totalAmount;
  }

  public List<ExpenseAttributeStatistics> list() {
    return list;
  }
}
