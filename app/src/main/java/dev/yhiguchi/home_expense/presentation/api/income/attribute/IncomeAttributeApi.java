package dev.yhiguchi.home_expense.presentation.api.income.attribute;

import dev.yhiguchi.home_expense.application.usecase.income.IncomeAttributeDeletionService;
import dev.yhiguchi.home_expense.application.usecase.income.IncomeAttributeRegistrationService;
import dev.yhiguchi.home_expense.application.usecase.income.IncomeAttributeRetrievalService;
import dev.yhiguchi.home_expense.application.usecase.income.IncomeAttributeUpdateService;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.presentation.api.IfMatchParser;
import dev.yhiguchi.home_expense.presentation.api.LinkHeaderCreatable;
import dev.yhiguchi.home_expense.query.Page;
import dev.yhiguchi.home_expense.query.Pagination;
import dev.yhiguchi.home_expense.query.PerPage;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchCriteria;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchResult;
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

@Path("/v1/income-attributes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IncomeAttributeApi implements LinkHeaderCreatable {

  IncomeAttributeRegistrationService incomeAttributeRegistrationService;
  IncomeAttributeUpdateService incomeAttributeUpdateService;
  IncomeAttributeDeletionService incomeAttributeDeletionService;

  IncomeAttributeRetrievalService incomeAttributeRetrievalService;

  public IncomeAttributeApi(
      IncomeAttributeRegistrationService incomeAttributeRegistrationService,
      IncomeAttributeUpdateService incomeAttributeUpdateService,
      IncomeAttributeDeletionService incomeAttributeDeletionService,
      IncomeAttributeRetrievalService incomeAttributeRetrievalService) {
    this.incomeAttributeRegistrationService = incomeAttributeRegistrationService;
    this.incomeAttributeUpdateService = incomeAttributeUpdateService;
    this.incomeAttributeDeletionService = incomeAttributeDeletionService;
    this.incomeAttributeRetrievalService = incomeAttributeRetrievalService;
  }

  @POST
  @RunOnVirtualThread
  public Response post(@Valid IncomeAttributePostRequest request, @Context UriInfo uriInfo) {
    IncomeAttributeIdentifier incomeAttributeIdentifier =
        incomeAttributeRegistrationService.createAndRegister(request.toIncomeAttributeName());
    URI uri = uriInfo.getAbsolutePathBuilder().path(incomeAttributeIdentifier.value()).build();
    return Response.created(uri).build();
  }

  @PUT
  @Path("{id}")
  @RunOnVirtualThread
  public Response put(
      @PathParam("id") @Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "IDの形式が不正です") String id,
      @Valid IncomeAttributePutRequest request,
      @HeaderParam("If-Match") String ifMatch) {
    long version = IfMatchParser.parse(ifMatch);
    incomeAttributeUpdateService.update(
        new IncomeAttributeIdentifier(id), request.toIncomeAttributeName(), version);
    return Response.noContent().build();
  }

  @DELETE
  @Path("{id}")
  @RunOnVirtualThread
  public Response delete(
      @PathParam("id") @Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "IDの形式が不正です") String id) {
    incomeAttributeDeletionService.delete(new IncomeAttributeIdentifier(id));
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
      @Context UriInfo uriInfo) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    IncomeAttributeSearchCriteria criteria = new IncomeAttributeSearchCriteria(pagination);
    IncomeAttributeSearchResult incomeAttributeSearchResult =
        incomeAttributeRetrievalService.search(criteria);
    IncomeAttributeGetListResponse response =
        page <= incomeAttributeSearchResult.totalCount()
            ? new IncomeAttributeGetListResponse(incomeAttributeSearchResult)
            : new IncomeAttributeGetListResponse();
    Response.ResponseBuilder responseBuilder = Response.ok(response);
    responseBuilder.header(
        "Link", create(uriInfo, pagination, incomeAttributeSearchResult.totalCount()));
    return responseBuilder.build();
  }

  @GET
  @Path("{id}")
  @RunOnVirtualThread
  public Response get(
      @PathParam("id") @Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "IDの形式が不正です") String id) {
    IncomeAttribute incomeAttribute =
        incomeAttributeRetrievalService.get(new IncomeAttributeIdentifier(id));
    IncomeAttributeGetResponse response = IncomeAttributeGetResponse.from(incomeAttribute);
    return Response.ok(response).tag(String.valueOf(incomeAttribute.version())).build();
  }
}
