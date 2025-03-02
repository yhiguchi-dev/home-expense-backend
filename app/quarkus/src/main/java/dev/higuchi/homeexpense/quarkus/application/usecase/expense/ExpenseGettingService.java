package dev.higuchi.homeexpense.quarkus.application.usecase.expense;

import dev.higuchi.homeexpense.command.model.expense.Expense;
import dev.higuchi.homeexpense.command.model.expense.ExpenseIdentifier;
import dev.higuchi.homeexpense.quarkus.application.service.expense.ExpenseAggregateService;
import dev.higuchi.homeexpense.quarkus.application.service.expense.ExpenseService;
import dev.higuchi.homeexpense.quarkus.application.service.expense.ExpenseSummaryService;
import dev.higuchi.homeexpense.query.model.expense.ExpenseAggregate;
import dev.higuchi.homeexpense.query.model.expense.ExpenseAggregateCriteria;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummary;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummaryCriteria;
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
