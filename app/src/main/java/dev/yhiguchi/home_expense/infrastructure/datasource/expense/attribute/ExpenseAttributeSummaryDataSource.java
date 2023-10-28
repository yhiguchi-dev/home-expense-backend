package dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummary;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummaryCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummaryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
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
