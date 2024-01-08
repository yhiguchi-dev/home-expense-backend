package dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSummary;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSummaryCriteria;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSummaryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class IncomeAttributeSummaryDataSource implements IncomeAttributeSummaryRepository {
  IncomeAttributeSummaryMapper incomeAttributeSummaryMapper;

  public IncomeAttributeSummaryDataSource(
      IncomeAttributeSummaryMapper incomeAttributeSummaryMapper) {
    this.incomeAttributeSummaryMapper = incomeAttributeSummaryMapper;
  }

  @Override
  public IncomeAttributeSummary find(IncomeAttributeSummaryCriteria criteria) {
    int count = incomeAttributeSummaryMapper.selectCount(criteria);
    if (count == 0) {
      return new IncomeAttributeSummary();
    }
    List<IncomeAttribute> list = incomeAttributeSummaryMapper.selectBy(criteria);
    return new IncomeAttributeSummary(count, list);
  }
}
