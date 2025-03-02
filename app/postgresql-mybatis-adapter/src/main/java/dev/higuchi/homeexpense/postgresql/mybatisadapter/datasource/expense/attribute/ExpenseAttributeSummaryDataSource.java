package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense.attribute;

import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummary;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummaryCriteria;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummaryRepository;
import java.util.List;

public class ExpenseAttributeSummaryDataSource implements ExpenseAttributeSummaryRepository {
  ExpenseAttributeSummaryMapper expenseAttributeSummaryMapper;

  public ExpenseAttributeSummaryDataSource(
      ExpenseAttributeSummaryMapper expenseAttributeSummaryMapper) {
    this.expenseAttributeSummaryMapper = expenseAttributeSummaryMapper;
  }

  @Override
  public ExpenseAttributeSummary find(ExpenseAttributeSummaryCriteria criteria) {
    int count = expenseAttributeSummaryMapper.selectCount(criteria);
    if (count == 0) {
      return new ExpenseAttributeSummary();
    }
    List<ExpenseAttribute> list = expenseAttributeSummaryMapper.selectBy(criteria);
    return new ExpenseAttributeSummary(count, list);
  }
}
