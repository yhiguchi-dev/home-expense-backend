package dev.yhiguchi.home_expense.application.service.expense.attribute;

import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummary;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummaryCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummaryRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseAttributeSummaryService {

  ExpenseAttributeSummaryRepository expenseAttributeSummaryRepository;

  public ExpenseAttributeSummaryService(
      ExpenseAttributeSummaryRepository expenseAttributeSummaryRepository) {
    this.expenseAttributeSummaryRepository = expenseAttributeSummaryRepository;
  }

  public ExpenseAttributeSummary find(ExpenseAttributeSummaryCriteria criteria) {
    return expenseAttributeSummaryRepository.find(criteria);
  }
}
