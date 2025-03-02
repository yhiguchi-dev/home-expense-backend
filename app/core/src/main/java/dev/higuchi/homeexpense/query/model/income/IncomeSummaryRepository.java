package dev.higuchi.homeexpense.query.model.income;

public interface IncomeSummaryRepository {
  IncomeSummary find(IncomeSummaryCriteria criteria);
}
