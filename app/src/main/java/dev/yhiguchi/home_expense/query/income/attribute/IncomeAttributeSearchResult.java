package dev.yhiguchi.home_expense.query.income.attribute;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import java.util.ArrayList;
import java.util.List;

public class IncomeAttributeSearchResult {

  Integer totalCount;

  List<IncomeAttribute> list;

  public IncomeAttributeSearchResult(Integer totalCount, List<IncomeAttribute> list) {
    this.totalCount = totalCount;
    this.list = list;
  }

  public IncomeAttributeSearchResult() {
    this(0, new ArrayList<>());
  }

  public Integer totalCount() {
    return totalCount;
  }

  public List<IncomeAttribute> list() {
    return list;
  }
}
