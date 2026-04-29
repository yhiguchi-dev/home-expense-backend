package dev.yhiguchi.home_expense.presentation.api.income;

import dev.yhiguchi.home_expense.application.usecase.income.IncomeDeletionService;
import dev.yhiguchi.home_expense.application.usecase.income.IncomeRegistrationService;
import dev.yhiguchi.home_expense.application.usecase.income.IncomeUpdateService;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.presentation.api.IfMatchParser;
import dev.yhiguchi.home_expense.presentation.api.LinkHeaderCreatable;
import dev.yhiguchi.home_expense.presentation.validation.IfMatch;
import dev.yhiguchi.home_expense.presentation.validation.PageNumber;
import dev.yhiguchi.home_expense.presentation.validation.PerPageSize;
import dev.yhiguchi.home_expense.presentation.validation.UuidFormat;
import dev.yhiguchi.home_expense.query.Page;
import dev.yhiguchi.home_expense.query.Pagination;
import dev.yhiguchi.home_expense.query.PerPage;
import dev.yhiguchi.home_expense.query.income.IncomeDetail;
import dev.yhiguchi.home_expense.query.income.IncomeSearchCriteria;
import dev.yhiguchi.home_expense.query.income.IncomeSearchResult;
import dev.yhiguchi.home_expense.query.income.IncomeSearchResultQuerier;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/v1/incomes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IncomeApi {

  IncomeRegistrationService incomeRegistrationService;
  IncomeUpdateService incomeUpdateService;
  IncomeDeletionService incomeDeletionService;
  IncomeSearchResultQuerier incomeSearchResultQuerier;

  public IncomeApi(
      IncomeRegistrationService incomeRegistrationService,
      IncomeUpdateService incomeUpdateService,
      IncomeDeletionService incomeDeletionService,
      IncomeSearchResultQuerier incomeSearchResultQuerier) {
    this.incomeRegistrationService = incomeRegistrationService;
    this.incomeUpdateService = incomeUpdateService;
    this.incomeDeletionService = incomeDeletionService;
    this.incomeSearchResultQuerier = incomeSearchResultQuerier;
  }

  @POST
  @RunOnVirtualThread
  public Response post(@Valid IncomePostRequest request, @Context UriInfo uriInfo) {
    IncomeIdentifier incomeIdentifier = incomeRegistrationService.register(request.toCommand());
    URI uri = uriInfo.getAbsolutePathBuilder().path(incomeIdentifier.value()).build();
    return Response.created(uri).build();
  }

  @PUT
  @Path("{id}")
  @RunOnVirtualThread
  public Response put(
      @PathParam("id") @UuidFormat String id,
      @Valid IncomePutRequest request,
      @HeaderParam("If-Match") @IfMatch String ifMatch) {
    incomeUpdateService.update(request.toCommand(id, IfMatchParser.parse(ifMatch)));
    return Response.noContent().build();
  }

  @DELETE
  @Path("{id}")
  @RunOnVirtualThread
  public Response delete(@PathParam("id") @UuidFormat String id) {
    incomeDeletionService.delete(new IncomeIdentifier(id));
    return Response.noContent().build();
  }

  @GET
  @RunOnVirtualThread
  public Response get(
      @QueryParam("page") @DefaultValue("1") @PageNumber Integer page,
      @QueryParam("per_page") @DefaultValue("20") @PerPageSize Integer perPage,
      @QueryParam("year") @Min(value = 1, message = "yearは{value}以上を指定してください") Integer year,
      @Context UriInfo uriInfo) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    IncomeSearchCriteria criteria = new IncomeSearchCriteria(pagination, year);
    IncomeSearchResult incomeSearchResult = incomeSearchResultQuerier.search(criteria);
    IncomeGetListResponse response = IncomeGetListResponse.from(incomeSearchResult, page);
    return Response.ok(response)
        .header(
            "Link",
            LinkHeaderCreatable.create(
                uriInfo, criteria.pagination(), incomeSearchResult.totalCount()))
        .build();
  }

  @GET
  @Path("{id}")
  @RunOnVirtualThread
  public Response get(@PathParam("id") @UuidFormat String id) {
    IncomeDetail detail = incomeSearchResultQuerier.get(new IncomeIdentifier(id));
    IncomeGetResponse response = IncomeGetResponse.from(detail);
    return Response.ok(response).tag(String.valueOf(detail.version())).build();
  }
}
