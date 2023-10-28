package dev.yhiguchi.home_expense.query.expense.attribute;

public interface ExpenseAttributeSummaryRepository {
  ExpenseAttributeSummary find(ExpenseAttributeSummaryCriteria criteria);
}
