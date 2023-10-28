package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummary;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummaryCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummaryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
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
