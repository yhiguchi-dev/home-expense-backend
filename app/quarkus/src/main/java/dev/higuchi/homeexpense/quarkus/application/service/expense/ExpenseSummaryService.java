package dev.higuchi.homeexpense.quarkus.application.service.expense;

import dev.higuchi.homeexpense.query.model.expense.ExpenseSummary;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummaryCriteria;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummaryRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseSummaryService {

  ExpenseSummaryRepository expenseSummaryRepository;

  public ExpenseSummaryService(ExpenseSummaryRepository expenseSummaryRepository) {
    this.expenseSummaryRepository = expenseSummaryRepository;
  }

  public ExpenseSummary find(ExpenseSummaryCriteria criteria) {
    return expenseSummaryRepository.find(criteria);
  }
}
