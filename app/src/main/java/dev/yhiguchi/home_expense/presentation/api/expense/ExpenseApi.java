package dev.yhiguchi.home_expense.presentation.api.expense;

import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseDeletionService;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseRegistrationService;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseRetrievalService;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseUpdateService;
import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.presentation.api.IfMatchParser;
import dev.yhiguchi.home_expense.presentation.api.LinkHeaderCreatable;
import dev.yhiguchi.home_expense.presentation.validation.ExpenseCategory;
import dev.yhiguchi.home_expense.query.expense.ExpenseCriteriaCreator;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResult;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResultQuerier;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
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
  ExpenseRetrievalService expenseRetrievalService;
  ExpenseSearchResultQuerier expenseSearchResultQuerier;
  ExpenseUpdateService expenseUpdateService;
  ExpenseDeletionService expenseDeletionService;

  public ExpenseApi(
      ExpenseRegistrationService expenseRegistrationService,
      ExpenseRetrievalService expenseRetrievalService,
      ExpenseSearchResultQuerier expenseSearchResultQuerier,
      ExpenseUpdateService expenseUpdateService,
      ExpenseDeletionService expenseDeletionService) {
    this.expenseRegistrationService = expenseRegistrationService;
    this.expenseRetrievalService = expenseRetrievalService;
    this.expenseSearchResultQuerier = expenseSearchResultQuerier;
    this.expenseUpdateService = expenseUpdateService;
    this.expenseDeletionService = expenseDeletionService;
  }

  @POST
  @RunOnVirtualThread
  public Response post(@Valid ExpensePostRequest request, @Context UriInfo uriInfo) {
    ExpenseIdentifier expenseIdentifier = expenseRegistrationService.register(request.toCommand());
    URI uri = uriInfo.getAbsolutePathBuilder().path(expenseIdentifier.value()).build();
    return Response.created(uri).build();
  }

  @PUT
  @Path("{id}")
  @RunOnVirtualThread
  public Response put(
      @PathParam("id") @Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "IDの形式が不正です") String id,
      @Valid ExpensePutRequest request,
      @HeaderParam("If-Match") String ifMatch) {
    expenseUpdateService.update(request.toCommand(id, IfMatchParser.parse(ifMatch)));
    return Response.noContent().build();
  }

  @DELETE
  @Path("{id}")
  @RunOnVirtualThread
  public Response delete(
      @PathParam("id") @Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "IDの形式が不正です") String id) {
    expenseDeletionService.delete(new ExpenseIdentifier(id));
    return Response.noContent().build();
  }

  @GET
  @RunOnVirtualThread
  public Response get(
      @QueryParam("page") @DefaultValue("1") @Min(value = 1, message = "pageは1以上を指定してください")
          Integer page,
      @QueryParam("per_page")
          @DefaultValue("20")
          @Min(value = 1, message = "per_pageは1以上を指定してください")
          @jakarta.validation.constraints.Max(value = 100, message = "per_pageは100以下を指定してください")
          Integer perPage,
      @QueryParam("year") @Min(value = 1, message = "yearは1以上を指定してください") Integer year,
      @QueryParam("month")
          @Min(value = 1, message = "monthは1〜12を指定してください")
          @jakarta.validation.constraints.Max(value = 12, message = "monthは1〜12を指定してください")
          Integer month,
      @QueryParam("category") @ExpenseCategory String category,
      @QueryParam("attribute_id") String attributeId,
      @Context UriInfo uriInfo) {
    ExpenseSearchCriteria criteria =
        ExpenseCriteriaCreator.create(page, perPage, year, month, category, attributeId);
    ExpenseSearchResult expenseSearchResult = expenseSearchResultQuerier.find(criteria);
    ExpenseGetListResponse response =
        page <= expenseSearchResult.totalCount()
            ? new ExpenseGetListResponse(expenseSearchResult)
            : new ExpenseGetListResponse();
    Response.ResponseBuilder responseBuilder = Response.ok(response);
    responseBuilder.header(
        "Link", create(uriInfo, criteria.pagination(), expenseSearchResult.totalCount()));
    return responseBuilder.build();
  }

  @GET
  @Path("{id}")
  @RunOnVirtualThread
  public Response get(
      @PathParam("id") @Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "IDの形式が不正です") String id) {
    Expense expense = expenseRetrievalService.get(new ExpenseIdentifier(id));
    ExpenseGetResponse response = ExpenseGetResponse.from(expense);
    return Response.ok(response).tag(String.valueOf(expense.version())).build();
  }
}
