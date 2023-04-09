package dev.yhiguchi.home_expense.query.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import java.util.ArrayList;
import java.util.List;

public class ExpenseAttributeSummary {

  Integer totalNumber;

  List<ExpenseAttribute> list;

  public ExpenseAttributeSummary(Integer totalNumber, List<ExpenseAttribute> list) {
    this.totalNumber = totalNumber;
    this.list = list;
  }

  public ExpenseAttributeSummary() {
    this(0, new ArrayList<>());
  }

  public Integer totalNumber() {
    return totalNumber;
  }

  public List<ExpenseAttribute> list() {
    return list;
  }
}
