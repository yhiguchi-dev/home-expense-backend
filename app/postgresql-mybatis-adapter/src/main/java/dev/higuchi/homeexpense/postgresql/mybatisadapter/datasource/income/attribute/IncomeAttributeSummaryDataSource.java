package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income.attribute;

import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummary;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummaryCriteria;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummaryRepository;
import java.util.List;

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
