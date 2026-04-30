package dev.yhiguchi.home_expense.presentation.api.income.attribute;

import dev.yhiguchi.home_expense.application.usecase.income.IncomeAttributeDeletionService;
import dev.yhiguchi.home_expense.application.usecase.income.IncomeAttributeRegistrationService;
import dev.yhiguchi.home_expense.application.usecase.income.IncomeAttributeUpdateService;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.presentation.api.IfMatchParser;
import dev.yhiguchi.home_expense.presentation.api.LinkHeaderCreatable;
import dev.yhiguchi.home_expense.presentation.validation.IfMatch;
import dev.yhiguchi.home_expense.presentation.validation.PageNumber;
import dev.yhiguchi.home_expense.presentation.validation.PerPageSize;
import dev.yhiguchi.home_expense.presentation.validation.UuidFormat;
import dev.yhiguchi.home_expense.query.Pagination;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeDetail;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchCriteria;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchResult;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchResultQuerier;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/v1/income-attributes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IncomeAttributeApi {

  IncomeAttributeRegistrationService incomeAttributeRegistrationService;
  IncomeAttributeUpdateService incomeAttributeUpdateService;
  IncomeAttributeDeletionService incomeAttributeDeletionService;
  IncomeAttributeSearchResultQuerier incomeAttributeSearchResultQuerier;

  public IncomeAttributeApi(
      IncomeAttributeRegistrationService incomeAttributeRegistrationService,
      IncomeAttributeUpdateService incomeAttributeUpdateService,
      IncomeAttributeDeletionService incomeAttributeDeletionService,
      IncomeAttributeSearchResultQuerier incomeAttributeSearchResultQuerier) {
    this.incomeAttributeRegistrationService = incomeAttributeRegistrationService;
    this.incomeAttributeUpdateService = incomeAttributeUpdateService;
    this.incomeAttributeDeletionService = incomeAttributeDeletionService;
    this.incomeAttributeSearchResultQuerier = incomeAttributeSearchResultQuerier;
  }

  @POST
  @RunOnVirtualThread
  public Response post(@Valid IncomeAttributePostRequest request, @Context UriInfo uriInfo) {
    IncomeAttributeIdentifier incomeAttributeIdentifier =
        incomeAttributeRegistrationService.register(request.toIncomeAttributeName());
    URI uri = uriInfo.getAbsolutePathBuilder().path(incomeAttributeIdentifier.value()).build();
    return Response.created(uri).build();
  }

  @PUT
  @Path("{id}")
  @RunOnVirtualThread
  public Response put(
      @PathParam("id") @UuidFormat String id,
      @Valid IncomeAttributePutRequest request,
      @HeaderParam("If-Match") @IfMatch String ifMatch) {
    incomeAttributeUpdateService.update(request.toCommand(id, IfMatchParser.parse(ifMatch)));
    return Response.noContent().build();
  }

  @DELETE
  @Path("{id}")
  @RunOnVirtualThread
  public Response delete(@PathParam("id") @UuidFormat String id) {
    incomeAttributeDeletionService.delete(new IncomeAttributeIdentifier(id));
    return Response.noContent().build();
  }

  @GET
  @RunOnVirtualThread
  public Response get(
      @QueryParam("page") @DefaultValue("1") @PageNumber Integer page,
      @QueryParam("per_page") @DefaultValue("20") @PerPageSize Integer perPage,
      @Context UriInfo uriInfo) {
    Pagination pagination = new Pagination(page, perPage);
    IncomeAttributeSearchCriteria criteria = new IncomeAttributeSearchCriteria(pagination);
    IncomeAttributeSearchResult incomeAttributeSearchResult =
        incomeAttributeSearchResultQuerier.search(criteria);
    IncomeAttributeGetListResponse response =
        IncomeAttributeGetListResponse.from(incomeAttributeSearchResult, page);
    return Response.ok(response)
        .header(
            "Link",
            LinkHeaderCreatable.create(
                uriInfo, pagination, incomeAttributeSearchResult.totalCount()))
        .build();
  }

  @GET
  @Path("{id}")
  @RunOnVirtualThread
  public Response get(@PathParam("id") @UuidFormat String id) {
    IncomeAttributeDetail detail =
        incomeAttributeSearchResultQuerier.get(new IncomeAttributeIdentifier(id));
    IncomeAttributeGetResponse response = IncomeAttributeGetResponse.from(detail);
    return Response.ok(response).tag(String.valueOf(detail.version())).build();
  }
}
