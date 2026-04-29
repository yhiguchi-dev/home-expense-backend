package dev.yhiguchi.home_expense.presentation.api.expense;

import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseDeletionService;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseRegistrationService;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseUpdateService;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.presentation.api.IfMatchParser;
import dev.yhiguchi.home_expense.presentation.api.LinkHeaderCreatable;
import dev.yhiguchi.home_expense.presentation.validation.ExpenseCategory;
import dev.yhiguchi.home_expense.presentation.validation.IfMatch;
import dev.yhiguchi.home_expense.presentation.validation.PageNumber;
import dev.yhiguchi.home_expense.presentation.validation.PerPageSize;
import dev.yhiguchi.home_expense.presentation.validation.UuidFormat;
import dev.yhiguchi.home_expense.query.expense.ExpenseCriteriaCreator;
import dev.yhiguchi.home_expense.query.expense.ExpenseDetail;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResult;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResultQuerier;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/v1/expenses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseApi {

  ExpenseRegistrationService expenseRegistrationService;
  ExpenseSearchResultQuerier expenseSearchResultQuerier;
  ExpenseUpdateService expenseUpdateService;
  ExpenseDeletionService expenseDeletionService;

  public ExpenseApi(
      ExpenseRegistrationService expenseRegistrationService,
      ExpenseSearchResultQuerier expenseSearchResultQuerier,
      ExpenseUpdateService expenseUpdateService,
      ExpenseDeletionService expenseDeletionService) {
    this.expenseRegistrationService = expenseRegistrationService;
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
      @PathParam("id") @UuidFormat String id,
      @Valid ExpensePutRequest request,
      @HeaderParam("If-Match") @IfMatch String ifMatch) {
    expenseUpdateService.update(request.toCommand(id, IfMatchParser.parse(ifMatch)));
    return Response.noContent().build();
  }

  @DELETE
  @Path("{id}")
  @RunOnVirtualThread
  public Response delete(@PathParam("id") @UuidFormat String id) {
    expenseDeletionService.delete(new ExpenseIdentifier(id));
    return Response.noContent().build();
  }

  @GET
  @RunOnVirtualThread
  public Response get(
      @QueryParam("page") @DefaultValue("1") @PageNumber Integer page,
      @QueryParam("per_page") @DefaultValue("20") @PerPageSize Integer perPage,
      @QueryParam("year") @Min(value = 1, message = "yearは{value}以上を指定してください") Integer year,
      @QueryParam("month")
          @Min(value = 1, message = "monthは1〜12を指定してください")
          @Max(value = 12, message = "monthは1〜12を指定してください")
          Integer month,
      @QueryParam("category") @ExpenseCategory String category,
      @QueryParam("attribute_id") String attributeId,
      @Context UriInfo uriInfo) {
    ExpenseSearchCriteria criteria =
        ExpenseCriteriaCreator.create(page, perPage, year, month, category, attributeId);
    ExpenseSearchResult expenseSearchResult = expenseSearchResultQuerier.search(criteria);
    ExpenseGetListResponse response = ExpenseGetListResponse.from(expenseSearchResult, page);
    return Response.ok(response)
        .header(
            "Link",
            LinkHeaderCreatable.create(
                uriInfo, criteria.pagination(), expenseSearchResult.totalCount()))
        .build();
  }

  @GET
  @Path("{id}")
  @RunOnVirtualThread
  public Response get(@PathParam("id") @UuidFormat String id) {
    ExpenseDetail detail = expenseSearchResultQuerier.get(new ExpenseIdentifier(id));
    ExpenseGetResponse response = ExpenseGetResponse.from(detail);
    return Response.ok(response).tag(String.valueOf(detail.version())).build();
  }
}
