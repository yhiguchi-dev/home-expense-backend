package dev.yhiguchi.home_expense.presentation.api.income;

import dev.yhiguchi.home_expense.application.usecase.income.IncomeDeletionService;
import dev.yhiguchi.home_expense.application.usecase.income.IncomeGettingService;
import dev.yhiguchi.home_expense.application.usecase.income.IncomeRegistrationService;
import dev.yhiguchi.home_expense.application.usecase.income.IncomeUpdateService;
import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.presentation.api.LinkHeaderCreatable;
import dev.yhiguchi.home_expense.query.Page;
import dev.yhiguchi.home_expense.query.Pagination;
import dev.yhiguchi.home_expense.query.PerPage;
import dev.yhiguchi.home_expense.query.income.IncomeSummary;
import dev.yhiguchi.home_expense.query.income.IncomeSummaryCriteria;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/v1/income")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IncomeApi implements LinkHeaderCreatable {

  IncomeRegistrationService incomeRegistrationService;
  IncomeUpdateService incomeUpdateService;
  IncomeDeletionService incomeDeletionService;
  IncomeGettingService incomeGettingService;

  public IncomeApi(
      IncomeRegistrationService incomeRegistrationService,
      IncomeUpdateService incomeUpdateService,
      IncomeDeletionService incomeDeletionService,
      IncomeGettingService incomeGettingService) {
    this.incomeRegistrationService = incomeRegistrationService;
    this.incomeUpdateService = incomeUpdateService;
    this.incomeDeletionService = incomeDeletionService;
    this.incomeGettingService = incomeGettingService;
  }

  @POST
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
  public Response put(@PathParam("id") String id, @Valid IncomePutRequest request) {
    incomeUpdateService.update(
        new IncomeIdentifier(id),
        request.toDescription(),
        request.toAmount(),
        request.toReceiveDate(),
        request.toIncomeAttributeIdentifier());
    return Response.noContent().build();
  }

  @DELETE
  @Path("{id}")
  public Response delete(@PathParam("id") String id) {
    incomeDeletionService.delete(new IncomeIdentifier(id));
    return Response.noContent().build();
  }

  @GET
  public Response get(
      @QueryParam("page") @DefaultValue("1") Integer page,
      @QueryParam("per_page") @DefaultValue("20") Integer perPage,
      @QueryParam("year") Integer year,
      @Context UriInfo uriInfo) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    IncomeSummaryCriteria criteria = new IncomeSummaryCriteria(pagination, year);
    IncomeSummary incomeSummary = incomeGettingService.findSummary(criteria);
    IncomeGetListResponse response =
        page <= incomeSummary.totalCount()
            ? new IncomeGetListResponse(incomeSummary)
            : new IncomeGetListResponse();
    Response.ResponseBuilder responseBuilder = Response.ok(response);
    responseBuilder.header(
        "Link", create(uriInfo, criteria.pagination(), incomeSummary.totalCount()));
    return responseBuilder.build();
  }

  @GET
  @Path("{id}")
  public Response get(@PathParam("id") String id) {
    Income income = incomeGettingService.get(new IncomeIdentifier(id));
    IncomeGetResponse response = IncomeGetResponse.from(income);
    return Response.ok(response).build();
  }
}
