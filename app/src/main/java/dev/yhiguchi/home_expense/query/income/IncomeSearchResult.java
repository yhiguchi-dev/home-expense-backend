package dev.yhiguchi.home_expense.query.income;

import dev.yhiguchi.home_expense.domain.model.income.Income;
import java.util.ArrayList;
import java.util.List;

public class IncomeSearchResult {

  Integer totalCount;

  List<Income> list;

  public IncomeSearchResult(Integer totalCount, List<Income> list) {
    this.totalCount = totalCount;
    this.list = list;
  }

  public IncomeSearchResult() {
    this(0, new ArrayList<>());
  }

  public Integer totalCount() {
    return totalCount;
  }

  public List<Income> list() {
    return list;
  }
}
