package dev.yhiguchi.home_expense.query.income.attribute;

public interface IncomeAttributeSummaryRepository {
  IncomeAttributeSummary find(IncomeAttributeSummaryCriteria criteria);
}
