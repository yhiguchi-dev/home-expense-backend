package dev.yhiguchi.home_expense.application.service;

import dev.yhiguchi.home_expense.application.service.expense.ExpenseAggregateService;
import dev.yhiguchi.home_expense.application.service.expense.ExpenseService;
import dev.yhiguchi.home_expense.application.service.expense.ExpenseSummaryService;
import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.query.expense.ExpenseAggregate;
import dev.yhiguchi.home_expense.query.expense.ExpenseAggregateCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummary;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummaryCriteria;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseGettingService {

  ExpenseService expenseService;
  ExpenseSummaryService expenseSummaryService;

  ExpenseAggregateService expenseAggregateService;

  public ExpenseGettingService(
      ExpenseService expenseService,
      ExpenseSummaryService expenseSummaryService,
      ExpenseAggregateService expenseAggregateService) {
    this.expenseService = expenseService;
    this.expenseSummaryService = expenseSummaryService;
    this.expenseAggregateService = expenseAggregateService;
  }

  public ExpenseSummary findSummary(ExpenseSummaryCriteria criteria) {
    return expenseSummaryService.find(criteria);
  }

  public Expense get(ExpenseIdentifier expenseIdentifier) {
    return expenseService.get(expenseIdentifier);
  }

  public ExpenseAggregate findAggregate(ExpenseAggregateCriteria criteria) {
    return expenseAggregateService.find(criteria);
  }
}
