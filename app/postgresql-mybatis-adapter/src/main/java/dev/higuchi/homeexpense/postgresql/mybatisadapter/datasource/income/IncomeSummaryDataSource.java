package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income;

import dev.higuchi.homeexpense.command.model.income.Income;
import dev.higuchi.homeexpense.query.model.income.IncomeSummary;
import dev.higuchi.homeexpense.query.model.income.IncomeSummaryCriteria;
import dev.higuchi.homeexpense.query.model.income.IncomeSummaryRepository;
import java.util.List;

public class IncomeSummaryDataSource implements IncomeSummaryRepository {
  IncomeSummaryMapper incomeSummaryMapper;

  public IncomeSummaryDataSource(IncomeSummaryMapper incomeSummaryMapper) {
    this.incomeSummaryMapper = incomeSummaryMapper;
  }

  @Override
  public IncomeSummary find(IncomeSummaryCriteria criteria) {
    int count = incomeSummaryMapper.selectCount(criteria);
    if (count == 0) {
      return new IncomeSummary();
    }
    List<Income> list = incomeSummaryMapper.selectBy(criteria);
    return new IncomeSummary(count, list);
  }
}
