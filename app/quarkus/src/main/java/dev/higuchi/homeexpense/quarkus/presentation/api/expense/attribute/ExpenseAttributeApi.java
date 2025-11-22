package dev.higuchi.homeexpense.quarkus.presentation.api.expense.attribute;

import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeAlreadyExistsException;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeConstraintException;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.network.LinkHeaderCreatable;
import dev.higuchi.homeexpense.quarkus.application.usecase.expense.ExpenseAttributeDeletionService;
import dev.higuchi.homeexpense.quarkus.application.usecase.expense.ExpenseAttributeGettingService;
import dev.higuchi.homeexpense.quarkus.application.usecase.expense.ExpenseAttributeRegistrationService;
import dev.higuchi.homeexpense.quarkus.application.usecase.expense.ExpenseAttributeUpdateService;
import dev.higuchi.homeexpense.quarkus.presentation.validation.ExpenseCategory;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummary;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummaryCriteria;
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
import java.util.Objects;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@Path("/v1/expense-attributes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseAttributeApi implements LinkHeaderCreatable {

  ExpenseAttributeRegistrationService expenseAttributeRegistrationService;
  ExpenseAttributeGettingService expenseAttributeGettingService;
  ExpenseAttributeUpdateService expenseAttributeUpdateService;

  ExpenseAttributeDeletionService expenseAttributeDeletionService;

  public ExpenseAttributeApi(
      ExpenseAttributeRegistrationService expenseAttributeRegistrationService,
      ExpenseAttributeGettingService expenseAttributeGettingService,
      ExpenseAttributeUpdateService expenseAttributeUpdateService,
      ExpenseAttributeDeletionService expenseAttributeDeletionService) {
    this.expenseAttributeRegistrationService = expenseAttributeRegistrationService;
    this.expenseAttributeGettingService = expenseAttributeGettingService;
    this.expenseAttributeUpdateService = expenseAttributeUpdateService;
    this.expenseAttributeDeletionService = expenseAttributeDeletionService;
  }

  @POST
  public Response post(@Valid ExpenseAttributePostRequest request, @Context UriInfo uriInfo) {
    ExpenseAttributeIdentifier expenseAttributeIdentifier =
        expenseAttributeRegistrationService.createAndRegister(
            request.toExpenseAttributeName(), request.toExpenseCategory());
    URI uri = uriInfo.getAbsolutePathBuilder().path(expenseAttributeIdentifier.value()).build();
    return Response.created(uri).build();
  }

  @PUT
  @Path("{id}")
  public Response put(@PathParam("id") String id, @Valid ExpenseAttributePutRequest request) {
    expenseAttributeUpdateService.update(
        new ExpenseAttributeIdentifier(id),
        request.toExpenseAttributeName(),
        request.toExpenseCategory());
    return Response.noContent().build();
  }

  @DELETE
  @Path("{id}")
  public Response delete(@PathParam("id") String id) {
    expenseAttributeDeletionService.delete(new ExpenseAttributeIdentifier(id));
    return Response.noContent().build();
  }

  @GET
  public Response get(
      @QueryParam("category") @ExpenseCategory String category,
      @QueryParam("page") @DefaultValue("1") Integer page,
      @QueryParam("per_page") @DefaultValue("20") Integer perPage,
      @Context UriInfo uriInfo) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    ExpenseAttributeSummaryCriteria criteria =
        Objects.nonNull(category)
            ? new ExpenseAttributeSummaryCriteria(
                dev.higuchi.homeexpense.command.model.expense.ExpenseCategory.of(category),
                pagination)
            : new ExpenseAttributeSummaryCriteria(pagination);
    ExpenseAttributeSummary expenseAttributeSummary =
        expenseAttributeGettingService.findSummary(criteria);
    ExpenseAttributeGetSummaryResponse response =
        page <= expenseAttributeSummary.totalCount()
            ? new ExpenseAttributeGetSummaryResponse(expenseAttributeSummary)
            : new ExpenseAttributeGetSummaryResponse();
    Response.ResponseBuilder responseBuilder = Response.ok(response);
    responseBuilder.header(
        "Link",
        create(uriInfo.getAbsolutePath(), pagination, expenseAttributeSummary.totalCount()));
    return responseBuilder.build();
  }

  @GET
  @Path("{id}")
  public Response get(@PathParam("id") String id) {
    ExpenseAttribute expenseAttribute =
        expenseAttributeGettingService.get(new ExpenseAttributeIdentifier(id));
    ExpenseAttributeGetResponse response = ExpenseAttributeGetResponse.from(expenseAttribute);
    return Response.ok(response).build();
  }

  @ServerExceptionMapper
  public RestResponse<String> mapException(ExpenseAttributeAlreadyExistsException e) {
    return RestResponse.status(Response.Status.BAD_REQUEST, "既に登録されています");
  }

  @ServerExceptionMapper
  public RestResponse<String> mapException(ExpenseAttributeConstraintException e) {
    return RestResponse.status(Response.Status.BAD_REQUEST, "制約があるため削除できません");
  }
}
