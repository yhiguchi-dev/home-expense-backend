package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import dev.yhiguchi.home_expense.application.service.ExpenseAttributeDeletionService;
import dev.yhiguchi.home_expense.application.service.ExpenseAttributeGettingService;
import dev.yhiguchi.home_expense.application.service.ExpenseAttributeRegistrationService;
import dev.yhiguchi.home_expense.application.service.ExpenseAttributeUpdateService;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeAlreadyExistsException;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeConstraintException;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.presentation.api.LinkHeaderCreatable;
import dev.yhiguchi.home_expense.presentation.validation.ExpenseCategory;
import dev.yhiguchi.home_expense.query.*;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummary;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummaryCriteria;
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
                dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory.of(category),
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
        "Link", create(uriInfo, pagination, expenseAttributeSummary.totalCount()));
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
