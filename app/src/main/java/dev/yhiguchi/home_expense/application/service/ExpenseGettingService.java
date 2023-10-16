package dev.yhiguchi.home_expense.application.service;

import dev.yhiguchi.home_expense.application.service.expense.ExpenseService;
import dev.yhiguchi.home_expense.application.service.expense.ExpenseSummaryService;
import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.query.expense.ExpenseCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummary;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseGettingService {

  ExpenseService expenseService;
  ExpenseSummaryService expenseSummaryService;

  public ExpenseGettingService(
      ExpenseService expenseService, ExpenseSummaryService expenseSummaryService) {
    this.expenseService = expenseService;
    this.expenseSummaryService = expenseSummaryService;
  }

  public ExpenseSummary findSummary(ExpenseCriteria criteria) {
    return expenseSummaryService.find(criteria);
  }

  public Expense get(ExpenseIdentifier expenseIdentifier) {
    return expenseService.get(expenseIdentifier);
  }
}
