package dev.yhiguchi.home_expense.presentation.api.expense;

import dev.yhiguchi.home_expense.application.service.ExpenseDeletionService;
import dev.yhiguchi.home_expense.application.service.ExpenseRegistrationService;
import dev.yhiguchi.home_expense.application.service.ExpenseUpdateService;
import dev.yhiguchi.home_expense.application.service.expense.ExpenseSummaryService;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.query.Page;
import dev.yhiguchi.home_expense.query.Pagination;
import dev.yhiguchi.home_expense.query.PerPage;
import dev.yhiguchi.home_expense.query.expense.ExpenseCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummary;
import java.net.URI;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/v1/expenses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseApi {

  ExpenseRegistrationService expenseRegistrationService;
  ExpenseSummaryService expenseSummaryService;
  ExpenseUpdateService expenseUpdateService;

  ExpenseDeletionService expenseDeletionService;

  public ExpenseApi(
      ExpenseRegistrationService expenseRegistrationService,
      ExpenseSummaryService expenseSummaryService,
      ExpenseUpdateService expenseUpdateService,
      ExpenseDeletionService expenseDeletionService) {
    this.expenseRegistrationService = expenseRegistrationService;
    this.expenseSummaryService = expenseSummaryService;
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
      @RestQuery("per_page") @DefaultValue("20") Integer perPage) {
    ExpenseCriteria criteria = toExpenseCriteria(page, perPage);
    ExpenseSummary expenseSummary = expenseSummaryService.find(criteria);
    ExpenseGetResponse response = new ExpenseGetResponse(criteria, expenseSummary);
    return Response.ok(response).build();
  }

  ExpenseCriteria toExpenseCriteria(Integer page, Integer perPage) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    return new ExpenseCriteria(pagination);
  }
}
