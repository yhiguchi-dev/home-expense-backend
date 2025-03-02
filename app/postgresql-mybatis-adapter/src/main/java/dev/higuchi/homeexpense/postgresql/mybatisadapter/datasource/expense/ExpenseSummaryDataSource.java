package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense;

import dev.higuchi.homeexpense.command.model.expense.Expense;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummary;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummaryCriteria;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummaryRepository;
import java.util.List;

public class ExpenseSummaryDataSource implements ExpenseSummaryRepository {
  ExpenseSummaryMapper expenseSummaryMapper;

  public ExpenseSummaryDataSource(ExpenseSummaryMapper expenseSummaryMapper) {
    this.expenseSummaryMapper = expenseSummaryMapper;
  }

  @Override
  public ExpenseSummary find(ExpenseSummaryCriteria criteria) {
    int count = expenseSummaryMapper.selectCount(criteria);
    if (count == 0) {
      return new ExpenseSummary();
    }
    List<Expense> list = expenseSummaryMapper.selectBy(criteria);
    return new ExpenseSummary(count, list);
  }
}
