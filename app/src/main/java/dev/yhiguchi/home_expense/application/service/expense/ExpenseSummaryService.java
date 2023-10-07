package dev.yhiguchi.home_expense.application.service.expense;

import dev.yhiguchi.home_expense.query.expense.ExpenseCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummary;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummaryRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseSummaryService {

  ExpenseSummaryRepository expenseSummaryRepository;

  public ExpenseSummaryService(ExpenseSummaryRepository expenseSummaryRepository) {
    this.expenseSummaryRepository = expenseSummaryRepository;
  }

  public ExpenseSummary find(ExpenseCriteria criteria) {
    return expenseSummaryRepository.find(criteria);
  }
}
