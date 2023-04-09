package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.query.expense.ExpenseCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummary;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummaryRepository;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseSummaryDataSource implements ExpenseSummaryRepository {
  ExpenseSummaryMapper expenseSummaryMapper;

  public ExpenseSummaryDataSource(ExpenseSummaryMapper expenseSummaryMapper) {
    this.expenseSummaryMapper = expenseSummaryMapper;
  }

  @Override
  public ExpenseSummary find(ExpenseCriteria criteria) {
    int count = expenseSummaryMapper.selectCount(criteria);
    if (count == 0) {
      return new ExpenseSummary();
    }
    List<Expense> list = expenseSummaryMapper.selectBy(criteria);
    return new ExpenseSummary(count, list);
  }
}
