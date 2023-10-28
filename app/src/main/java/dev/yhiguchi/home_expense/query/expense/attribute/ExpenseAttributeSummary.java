package dev.yhiguchi.home_expense.query.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import java.util.ArrayList;
import java.util.List;

public class ExpenseAttributeSummary {

  Integer totalCount;

  List<ExpenseAttribute> list;

  public ExpenseAttributeSummary(Integer totalCount, List<ExpenseAttribute> list) {
    this.totalCount = totalCount;
    this.list = list;
  }

  public ExpenseAttributeSummary() {
    this(0, new ArrayList<>());
  }

  public Integer totalCount() {
    return totalCount;
  }

  public List<ExpenseAttribute> list() {
    return list;
  }
}
