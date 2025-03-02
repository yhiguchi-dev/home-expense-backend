package dev.higuchi.homeexpense.query.model.income;

import dev.higuchi.homeexpense.command.model.income.Income;
import java.util.ArrayList;
import java.util.List;

public class IncomeSummary {

  Integer totalCount;

  List<Income> list;

  public IncomeSummary(Integer totalCount, List<Income> list) {
    this.totalCount = totalCount;
    this.list = list;
  }

  public IncomeSummary() {
    this(0, new ArrayList<>());
  }

  public Integer totalCount() {
    return totalCount;
  }

  public List<Income> list() {
    return list;
  }
}
