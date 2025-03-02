package dev.higuchi.homeexpense.springboot.application.service.expense;

import dev.higuchi.homeexpense.query.model.expense.ExpenseSummary;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummaryCriteria;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummaryRepository;
import org.springframework.stereotype.Service;

@Service
public class ExpenseSummaryService {

  ExpenseSummaryRepository expenseSummaryRepository;

  public ExpenseSummaryService(ExpenseSummaryRepository expenseSummaryRepository) {
    this.expenseSummaryRepository = expenseSummaryRepository;
  }

  public ExpenseSummary find(ExpenseSummaryCriteria criteria) {
    return expenseSummaryRepository.find(criteria);
  }
}
