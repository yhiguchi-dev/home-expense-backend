package dev.yhiguchi.home_expense.presentation.api.expense;

import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseDeletionService;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseGettingService;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseRegistrationService;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseUpdateService;
import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.presentation.api.LinkHeaderCreatable;
import dev.yhiguchi.home_expense.presentation.validation.ExpenseCategory;
import dev.yhiguchi.home_expense.query.expense.ExpenseCriteriaCreator;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummary;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummaryCriteria;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

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
      @QueryParam("page") @DefaultValue("1") Integer page,
      @QueryParam("per_page") @DefaultValue("20") Integer perPage,
      @QueryParam("year") Integer year,
      @QueryParam("month") Integer month,
      @QueryParam("category") @ExpenseCategory String category,
      @QueryParam("attribute_name") String attributeName,
      @Context UriInfo uriInfo) {
    ExpenseSummaryCriteria criteria =
        ExpenseCriteriaCreator.create(page, perPage, year, month, category, attributeName);
    ExpenseSummary expenseSummary = expenseGettingService.findSummary(criteria);
    ExpenseGetListResponse response =
        page <= expenseSummary.totalCount()
            ? new ExpenseGetListResponse(expenseSummary)
            : new ExpenseGetListResponse();
    Response.ResponseBuilder responseBuilder = Response.ok(response);
    responseBuilder.header(
        "Link", create(uriInfo, criteria.pagination(), expenseSummary.totalCount()));
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
