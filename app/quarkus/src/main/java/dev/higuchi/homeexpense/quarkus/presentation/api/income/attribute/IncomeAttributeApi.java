package dev.higuchi.homeexpense.quarkus.presentation.api.income.attribute;

import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeAlreadyExistsException;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeConstraintException;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeIdentifier;
import dev.higuchi.homeexpense.network.LinkHeaderCreatable;
import dev.higuchi.homeexpense.quarkus.application.usecase.income.IncomeAttributeDeletionService;
import dev.higuchi.homeexpense.quarkus.application.usecase.income.IncomeAttributeGettingService;
import dev.higuchi.homeexpense.quarkus.application.usecase.income.IncomeAttributeRegistrationService;
import dev.higuchi.homeexpense.quarkus.application.usecase.income.IncomeAttributeUpdateService;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummary;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummaryCriteria;
import dev.higuchi.homeexpense.query.model.pagination.Page;
import dev.higuchi.homeexpense.query.model.pagination.Pagination;
import dev.higuchi.homeexpense.query.model.pagination.PerPage;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@Path("/v1/income-attributes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IncomeAttributeApi implements LinkHeaderCreatable {

  IncomeAttributeRegistrationService incomeAttributeRegistrationService;
  IncomeAttributeUpdateService incomeAttributeUpdateService;
  IncomeAttributeDeletionService incomeAttributeDeletionService;

  IncomeAttributeGettingService incomeAttributeGettingService;

  public IncomeAttributeApi(
      IncomeAttributeRegistrationService incomeAttributeRegistrationService,
      IncomeAttributeUpdateService incomeAttributeUpdateService,
      IncomeAttributeDeletionService incomeAttributeDeletionService,
      IncomeAttributeGettingService incomeAttributeGettingService) {
    this.incomeAttributeRegistrationService = incomeAttributeRegistrationService;
    this.incomeAttributeUpdateService = incomeAttributeUpdateService;
    this.incomeAttributeDeletionService = incomeAttributeDeletionService;
    this.incomeAttributeGettingService = incomeAttributeGettingService;
  }

  @POST
  public Response post(@Valid IncomeAttributePostRequest request, @Context UriInfo uriInfo) {
    IncomeAttributeIdentifier incomeAttributeIdentifier =
        incomeAttributeRegistrationService.createAndRegister(request.toIncomeAttributeName());
    URI uri = uriInfo.getAbsolutePathBuilder().path(incomeAttributeIdentifier.value()).build();
    return Response.created(uri).build();
  }

  @PUT
  @Path("{id}")
  public Response put(@PathParam("id") String id, @Valid IncomeAttributePutRequest request) {
    incomeAttributeUpdateService.update(
        new IncomeAttributeIdentifier(id), request.toIncomeAttributeName());
    return Response.noContent().build();
  }

  @DELETE
  @Path("{id}")
  public Response delete(@PathParam("id") String id) {
    incomeAttributeDeletionService.delete(new IncomeAttributeIdentifier(id));
    return Response.noContent().build();
  }

  @GET
  public Response get(
      @QueryParam("page") @DefaultValue("1") Integer page,
      @QueryParam("per_page") @DefaultValue("20") Integer perPage,
      @Context UriInfo uriInfo) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    IncomeAttributeSummaryCriteria criteria = new IncomeAttributeSummaryCriteria(pagination);
    IncomeAttributeSummary incomeAttributeSummary =
        incomeAttributeGettingService.findSummary(criteria);
    IncomeAttributeGetSummaryResponse response =
        page <= incomeAttributeSummary.totalCount()
            ? new IncomeAttributeGetSummaryResponse(incomeAttributeSummary)
            : new IncomeAttributeGetSummaryResponse();
    Response.ResponseBuilder responseBuilder = Response.ok(response);
    responseBuilder.header(
        "Link", create(uriInfo.getAbsolutePath(), pagination, incomeAttributeSummary.totalCount()));
    return responseBuilder.build();
  }

  @GET
  @Path("{id}")
  public Response get(@PathParam("id") String id) {
    IncomeAttribute incomeAttribute =
        incomeAttributeGettingService.get(new IncomeAttributeIdentifier(id));
    IncomeAttributeGetResponse response = IncomeAttributeGetResponse.from(incomeAttribute);
    return Response.ok(response).build();
  }

  @ServerExceptionMapper
  public RestResponse<String> mapException(IncomeAttributeAlreadyExistsException e) {
    return RestResponse.status(Response.Status.BAD_REQUEST, "既に登録されています");
  }

  @ServerExceptionMapper
  public RestResponse<String> mapException(IncomeAttributeConstraintException e) {
    return RestResponse.status(Response.Status.BAD_REQUEST, "制約があるため削除できません");
  }
}
