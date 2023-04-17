package dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummary;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummaryRepository;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ExpenseAttributeSummaryDataSource implements ExpenseAttributeSummaryRepository {
  @Inject ExpenseAttributeSummaryMapper expenseAttributeSummaryMapper;

  @Override
  public ExpenseAttributeSummary find(ExpenseAttributeCriteria criteria) {
    int count = expenseAttributeSummaryMapper.selectCount(criteria);
    if (count == 0) {
      return new ExpenseAttributeSummary();
    }
    List<ExpenseAttribute> list = expenseAttributeSummaryMapper.selectBy(criteria);
    return new ExpenseAttributeSummary(count, list);
  }
}
