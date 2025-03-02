package dev.higuchi.homeexpense.quarkus.presentation.api.expense;

import dev.higuchi.homeexpense.quarkus.application.usecase.expense.ExpenseGettingService;
import dev.higuchi.homeexpense.query.model.expense.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/expenses/aggregate")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseAggregateApi {

  ExpenseGettingService expenseGettingService;

  public ExpenseAggregateApi(ExpenseGettingService expenseGettingService) {
    this.expenseGettingService = expenseGettingService;
  }

  @GET
  public Response get(@QueryParam("year") int year, @QueryParam("month") int month) {
    ExpenseAggregateCriteria criteria = new ExpenseAggregateCriteria(year, month);
    ExpenseAggregate aggregate = expenseGettingService.findAggregate(criteria);
    ExpenseGetAggregateResponse response = new ExpenseGetAggregateResponse(aggregate);
    return Response.ok(response).build();
  }
}
