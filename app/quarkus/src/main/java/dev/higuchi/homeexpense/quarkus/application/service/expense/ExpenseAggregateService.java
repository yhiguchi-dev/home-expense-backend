package dev.higuchi.homeexpense.quarkus.application.service.expense;

import dev.higuchi.homeexpense.query.model.expense.*;
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
