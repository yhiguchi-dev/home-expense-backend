package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseRepository;
import dev.yhiguchi.home_expense.query.expense.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseGettingService {

  ExpenseRepository expenseRepository;
  ExpenseSummaryRepository expenseSummaryRepository;
  ExpenseAggregateRepository expenseAggregateRepository;

  public ExpenseGettingService(
      ExpenseRepository expenseRepository,
      ExpenseSummaryRepository expenseSummaryRepository,
      ExpenseAggregateRepository expenseAggregateRepository) {
    this.expenseRepository = expenseRepository;
    this.expenseSummaryRepository = expenseSummaryRepository;
    this.expenseAggregateRepository = expenseAggregateRepository;
  }

  public ExpenseSummary findSummary(ExpenseSummaryCriteria criteria) {
    return expenseSummaryRepository.find(criteria);
  }

  public Expense get(ExpenseIdentifier expenseIdentifier) {
    return expenseRepository.get(expenseIdentifier);
  }

  public ExpenseAggregate findAggregate(ExpenseAggregateCriteria criteria) {
    return expenseAggregateRepository.find(criteria);
  }
}
