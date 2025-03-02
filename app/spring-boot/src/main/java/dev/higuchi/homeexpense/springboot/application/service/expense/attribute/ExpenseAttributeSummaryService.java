package dev.higuchi.homeexpense.springboot.application.service.expense.attribute;

import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummary;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummaryCriteria;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummaryRepository;
import org.springframework.stereotype.Service;

@Service
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
