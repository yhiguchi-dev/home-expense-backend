package dev.higuchi.homeexpense.springboot.presentation.api.expense;

import dev.higuchi.homeexpense.query.model.expense.ExpenseAggregate;
import dev.higuchi.homeexpense.query.model.expense.ExpenseAggregateCriteria;
import dev.higuchi.homeexpense.springboot.application.usecase.expense.ExpenseGettingService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    path = "/v1/expenses/aggregate",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class ExpenseAggregateApi {

  ExpenseGettingService expenseGettingService;

  public ExpenseAggregateApi(ExpenseGettingService expenseGettingService) {
    this.expenseGettingService = expenseGettingService;
  }

  @GetMapping
  public ResponseEntity<?> get(@RequestParam("year") int year, @RequestParam("month") int month) {
    ExpenseAggregateCriteria criteria = new ExpenseAggregateCriteria(year, month);
    ExpenseAggregate aggregate = expenseGettingService.findAggregate(criteria);
    ExpenseGetAggregateResponse response = new ExpenseGetAggregateResponse(aggregate);
    return ResponseEntity.ok(response);
  }
}
