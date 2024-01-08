package dev.yhiguchi.home_expense.infrastructure.datasource.income;

import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.query.income.IncomeSummary;
import dev.yhiguchi.home_expense.query.income.IncomeSummaryCriteria;
import dev.yhiguchi.home_expense.query.income.IncomeSummaryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
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
