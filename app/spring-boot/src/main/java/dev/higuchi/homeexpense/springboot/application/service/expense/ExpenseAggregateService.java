package dev.higuchi.homeexpense.springboot.application.service.expense;

import dev.higuchi.homeexpense.query.model.expense.ExpenseAggregate;
import dev.higuchi.homeexpense.query.model.expense.ExpenseAggregateCriteria;
import dev.higuchi.homeexpense.query.model.expense.ExpenseAggregateRepository;
import org.springframework.stereotype.Service;

@Service
public class ExpenseAggregateService {

  ExpenseAggregateRepository expenseAggregateRepository;

  public ExpenseAggregateService(ExpenseAggregateRepository expenseAggregateRepository) {
    this.expenseAggregateRepository = expenseAggregateRepository;
  }

  public ExpenseAggregate find(ExpenseAggregateCriteria criteria) {
    return expenseAggregateRepository.find(criteria);
  }
}
