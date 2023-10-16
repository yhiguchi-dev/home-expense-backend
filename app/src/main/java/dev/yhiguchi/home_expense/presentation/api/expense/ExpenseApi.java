package dev.yhiguchi.home_expense.presentation.api.expense;

import dev.yhiguchi.home_expense.application.service.ExpenseDeletionService;
import dev.yhiguchi.home_expense.application.service.ExpenseGettingService;
import dev.yhiguchi.home_expense.application.service.ExpenseRegistrationService;
import dev.yhiguchi.home_expense.application.service.ExpenseUpdateService;
import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.presentation.api.LinkHeaderCreatable;
import dev.yhiguchi.home_expense.query.Page;
import dev.yhiguchi.home_expense.query.Pagination;
import dev.yhiguchi.home_expense.query.PerPage;
import dev.yhiguchi.home_expense.query.expense.ExpenseCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummary;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/v1/expenses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseApi implements LinkHeaderCreatable {

  ExpenseRegistrationService expenseRegistrationService;
  ExpenseGettingService expenseGettingService;
  ExpenseUpdateService expenseUpdateService;
  ExpenseDeletionService expenseDeletionService;

  public ExpenseApi(
      ExpenseRegistrationService expenseRegistrationService,
      ExpenseGettingService expenseGettingService,
      ExpenseUpdateService expenseUpdateService,
      ExpenseDeletionService expenseDeletionService) {
    this.expenseRegistrationService = expenseRegistrationService;
    this.expenseGettingService = expenseGettingService;
    this.expenseUpdateService = expenseUpdateService;
    this.expenseDeletionService = expenseDeletionService;
  }

  @POST
  public Response post(@Valid ExpensePostRequest request, @Context UriInfo uriInfo) {
    ExpenseIdentifier expenseIdentifier =
        expenseRegistrationService.createAndRegister(
            request.toDescription(),
            request.toPrice(),
            request.toPaymentDate(),
            request.toExpenseAttributeIdentifier());
    URI uri = uriInfo.getAbsolutePathBuilder().path(expenseIdentifier.value()).build();
    return Response.created(uri).build();
  }

  @PUT
  @Path("{id}")
  public Response put(@PathParam("id") String id, @Valid ExpensePutRequest request) {
    expenseUpdateService.update(
        new ExpenseIdentifier(id),
        request.toDescription(),
        request.toPrice(),
        request.toPaymentDate(),
        request.toExpenseAttributeIdentifier());
    return Response.noContent().build();
  }

  @DELETE
  @Path("{id}")
  public Response delete(@PathParam("id") String id) {
    expenseDeletionService.delete(new ExpenseIdentifier(id));
    return Response.noContent().build();
  }

  @GET
  public Response get(
      @RestQuery("page") @DefaultValue("1") Integer page,
      @RestQuery("per_page") @DefaultValue("20") Integer perPage,
      @Context UriInfo uriInfo) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    ExpenseCriteria criteria = new ExpenseCriteria(pagination);
    ExpenseSummary expenseSummary = expenseGettingService.findSummary(criteria);
    ExpenseGetSummaryResponse response = new ExpenseGetSummaryResponse(expenseSummary);
    Response.ResponseBuilder responseBuilder = Response.ok(response);
    responseBuilder.header("Link", create(uriInfo, pagination, expenseSummary.totalNumber()));
    return responseBuilder.build();
  }

  @GET
  @Path("{id}")
  public Response get(@PathParam("id") String id) {
    Expense expense = expenseGettingService.get(new ExpenseIdentifier(id));
    ExpenseGetResponse response = ExpenseGetResponse.from(expense);
    return Response.ok(response).build();
  }
}
