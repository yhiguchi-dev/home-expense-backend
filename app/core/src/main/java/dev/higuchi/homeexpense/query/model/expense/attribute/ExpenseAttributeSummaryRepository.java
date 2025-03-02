package dev.higuchi.homeexpense.query.model.expense.attribute;

public interface ExpenseAttributeSummaryRepository {
  ExpenseAttributeSummary find(ExpenseAttributeSummaryCriteria criteria);
}
