package dev.yhiguchi.home_expense.presentation.api.income;

import dev.yhiguchi.home_expense.application.usecase.income.IncomeDeletionService;
import dev.yhiguchi.home_expense.application.usecase.income.IncomeRegistrationService;
import dev.yhiguchi.home_expense.application.usecase.income.IncomeRetrievalService;
import dev.yhiguchi.home_expense.application.usecase.income.IncomeUpdateService;
import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.presentation.api.IfMatchParser;
import dev.yhiguchi.home_expense.presentation.api.LinkHeaderCreatable;
import dev.yhiguchi.home_expense.query.Page;
import dev.yhiguchi.home_expense.query.Pagination;
import dev.yhiguchi.home_expense.query.PerPage;
import dev.yhiguchi.home_expense.query.income.IncomeSearchCriteria;
import dev.yhiguchi.home_expense.query.income.IncomeSearchResult;
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

@Path("/v1/incomes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IncomeApi implements LinkHeaderCreatable {

  IncomeRegistrationService incomeRegistrationService;
  IncomeUpdateService incomeUpdateService;
  IncomeDeletionService incomeDeletionService;
  IncomeRetrievalService incomeRetrievalService;

  public IncomeApi(
      IncomeRegistrationService incomeRegistrationService,
      IncomeUpdateService incomeUpdateService,
      IncomeDeletionService incomeDeletionService,
      IncomeRetrievalService incomeRetrievalService) {
    this.incomeRegistrationService = incomeRegistrationService;
    this.incomeUpdateService = incomeUpdateService;
    this.incomeDeletionService = incomeDeletionService;
    this.incomeRetrievalService = incomeRetrievalService;
  }

  @POST
  @RunOnVirtualThread
  public Response post(@Valid IncomePostRequest request, @Context UriInfo uriInfo) {
    IncomeIdentifier incomeIdentifier =
        incomeRegistrationService.createAndRegister(
            request.toDescription(),
            request.toAmount(),
            request.toReceiveDate(),
            request.toIncomeAttributeIdentifier());
    URI uri = uriInfo.getAbsolutePathBuilder().path(incomeIdentifier.value()).build();
    return Response.created(uri).build();
  }

  @PUT
  @Path("{id}")
  @RunOnVirtualThread
  public Response put(
      @PathParam("id") @Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "IDの形式が不正です") String id,
      @Valid IncomePutRequest request,
      @HeaderParam("If-Match") String ifMatch) {
    long version = IfMatchParser.parse(ifMatch);
    incomeUpdateService.update(
        new IncomeIdentifier(id),
        request.toDescription(),
        request.toAmount(),
        request.toReceiveDate(),
        request.toIncomeAttributeIdentifier(),
        version);
    return Response.noContent().build();
  }

  @DELETE
  @Path("{id}")
  @RunOnVirtualThread
  public Response delete(
      @PathParam("id") @Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "IDの形式が不正です") String id) {
    incomeDeletionService.delete(new IncomeIdentifier(id));
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
      @Context UriInfo uriInfo) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    IncomeSearchCriteria criteria = new IncomeSearchCriteria(pagination, year);
    IncomeSearchResult incomeSearchResult = incomeRetrievalService.search(criteria);
    IncomeGetListResponse response =
        page <= incomeSearchResult.totalCount()
            ? new IncomeGetListResponse(incomeSearchResult)
            : new IncomeGetListResponse();
    Response.ResponseBuilder responseBuilder = Response.ok(response);
    responseBuilder.header(
        "Link", create(uriInfo, criteria.pagination(), incomeSearchResult.totalCount()));
    return responseBuilder.build();
  }

  @GET
  @Path("{id}")
  @RunOnVirtualThread
  public Response get(
      @PathParam("id") @Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "IDの形式が不正です") String id) {
    Income income = incomeRetrievalService.get(new IncomeIdentifier(id));
    IncomeGetResponse response = IncomeGetResponse.from(income);
    return Response.ok(response).tag(String.valueOf(income.version())).build();
  }
}
