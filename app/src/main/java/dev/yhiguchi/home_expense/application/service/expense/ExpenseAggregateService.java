package dev.yhiguchi.home_expense.application.service.expense;

import dev.yhiguchi.home_expense.query.expense.*;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseAggregateService {

  ExpenseAggregateRepository expenseAggregateRepository;

  public ExpenseAggregateService(ExpenseAggregateRepository expenseAggregateRepository) {
    this.expenseAggregateRepository = expenseAggregateRepository;
  }

  public ExpenseAggregate find(ExpenseAggregateCriteria criteria) {
    return expenseAggregateRepository.find(criteria);
  }
}
