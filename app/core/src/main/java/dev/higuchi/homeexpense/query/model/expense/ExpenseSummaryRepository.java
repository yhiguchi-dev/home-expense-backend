package dev.higuchi.homeexpense.query.model.expense;

public interface ExpenseSummaryRepository {
  ExpenseSummary find(ExpenseSummaryCriteria criteria);
}
