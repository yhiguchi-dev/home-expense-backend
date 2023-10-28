package dev.yhiguchi.home_expense.presentation.api.expense;

import dev.yhiguchi.home_expense.application.service.ExpenseGettingService;
import dev.yhiguchi.home_expense.query.expense.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/v1/expenses/aggregate")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseAggregateApi {

  ExpenseGettingService expenseGettingService;

  public ExpenseAggregateApi(ExpenseGettingService expenseGettingService) {
    this.expenseGettingService = expenseGettingService;
  }

  @GET
  public Response get(@RestQuery("year") int year, @RestQuery("month") int month) {
    ExpenseAggregateCriteria criteria = new ExpenseAggregateCriteria(year, month);
    ExpenseAggregate aggregate = expenseGettingService.findAggregate(criteria);
    ExpenseGetAggregateResponse response = new ExpenseGetAggregateResponse(aggregate);
    return Response.ok(response).build();
  }
}
