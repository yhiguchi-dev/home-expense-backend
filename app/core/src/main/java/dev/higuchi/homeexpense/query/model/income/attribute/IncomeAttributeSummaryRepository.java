package dev.higuchi.homeexpense.query.model.income.attribute;

public interface IncomeAttributeSummaryRepository {
  IncomeAttributeSummary find(IncomeAttributeSummaryCriteria criteria);
}
