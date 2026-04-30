package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseAttributeDeletionService;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseAttributeRegistrationService;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseAttributeUpdateService;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.presentation.api.IfMatchParser;
import dev.yhiguchi.home_expense.presentation.api.LinkHeaderCreatable;
import dev.yhiguchi.home_expense.presentation.validation.ExpenseCategory;
import dev.yhiguchi.home_expense.presentation.validation.IfMatch;
import dev.yhiguchi.home_expense.presentation.validation.PageNumber;
import dev.yhiguchi.home_expense.presentation.validation.PerPageSize;
import dev.yhiguchi.home_expense.presentation.validation.UuidFormat;
import dev.yhiguchi.home_expense.query.*;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeDetail;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSearchCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSearchResult;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSearchResultQuerier;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/v1/expense-attributes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseAttributeApi {

  ExpenseAttributeRegistrationService expenseAttributeRegistrationService;
  ExpenseAttributeSearchResultQuerier expenseAttributeSearchResultQuerier;
  ExpenseAttributeUpdateService expenseAttributeUpdateService;

  ExpenseAttributeDeletionService expenseAttributeDeletionService;

  public ExpenseAttributeApi(
      ExpenseAttributeRegistrationService expenseAttributeRegistrationService,
      ExpenseAttributeSearchResultQuerier expenseAttributeSearchResultQuerier,
      ExpenseAttributeUpdateService expenseAttributeUpdateService,
      ExpenseAttributeDeletionService expenseAttributeDeletionService) {
    this.expenseAttributeRegistrationService = expenseAttributeRegistrationService;
    this.expenseAttributeSearchResultQuerier = expenseAttributeSearchResultQuerier;
    this.expenseAttributeUpdateService = expenseAttributeUpdateService;
    this.expenseAttributeDeletionService = expenseAttributeDeletionService;
  }

  @POST
  @RunOnVirtualThread
  public Response post(@Valid ExpenseAttributePostRequest request, @Context UriInfo uriInfo) {
    ExpenseAttributeIdentifier expenseAttributeIdentifier =
        expenseAttributeRegistrationService.register(request.toCommand());
    URI uri = uriInfo.getAbsolutePathBuilder().path(expenseAttributeIdentifier.value()).build();
    return Response.created(uri).build();
  }

  @PUT
  @Path("{id}")
  @RunOnVirtualThread
  public Response put(
      @PathParam("id") @UuidFormat String id,
      @Valid ExpenseAttributePutRequest request,
      @HeaderParam("If-Match") @IfMatch String ifMatch) {
    expenseAttributeUpdateService.update(request.toCommand(id, IfMatchParser.parse(ifMatch)));
    return Response.noContent().build();
  }

  @DELETE
  @Path("{id}")
  @RunOnVirtualThread
  public Response delete(@PathParam("id") @UuidFormat String id) {
    expenseAttributeDeletionService.delete(new ExpenseAttributeIdentifier(id));
    return Response.noContent().build();
  }

  @GET
  @RunOnVirtualThread
  public Response get(
      @QueryParam("category") @ExpenseCategory String category,
      @QueryParam("page") @DefaultValue("1") @PageNumber Integer page,
      @QueryParam("per_page") @DefaultValue("20") @PerPageSize Integer perPage,
      @Context UriInfo uriInfo) {
    Pagination pagination = new Pagination(page, perPage);
    ExpenseAttributeSearchCriteria criteria =
        new ExpenseAttributeSearchCriteria(category, pagination);
    ExpenseAttributeSearchResult expenseAttributeSearchResult =
        expenseAttributeSearchResultQuerier.search(criteria);
    ExpenseAttributeGetListResponse response =
        ExpenseAttributeGetListResponse.from(expenseAttributeSearchResult, page);
    return Response.ok(response)
        .header(
            "Link",
            LinkHeaderCreatable.create(
                uriInfo, pagination, expenseAttributeSearchResult.totalCount()))
        .build();
  }

  @GET
  @Path("{id}")
  @RunOnVirtualThread
  public Response get(@PathParam("id") @UuidFormat String id) {
    ExpenseAttributeDetail detail =
        expenseAttributeSearchResultQuerier.get(new ExpenseAttributeIdentifier(id));
    ExpenseAttributeGetResponse response = ExpenseAttributeGetResponse.from(detail);
    return Response.ok(response).tag(String.valueOf(detail.version())).build();
  }
}
