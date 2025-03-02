package dev.higuchi.homeexpense.quarkus.application.service.expense.attribute;

import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummary;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummaryCriteria;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummaryRepository;
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
