package dev.yhiguchi.home_expense.query.income;

public interface IncomeSummaryRepository {
  IncomeSummary find(IncomeSummaryCriteria criteria);
}
