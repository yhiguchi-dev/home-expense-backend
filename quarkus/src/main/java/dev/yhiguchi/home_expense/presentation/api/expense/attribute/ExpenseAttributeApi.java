package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import dev.yhiguchi.home_expense.application.service.ExpenseAttributeDeletionService;
import dev.yhiguchi.home_expense.application.service.ExpenseAttributeRegistrationService;
import dev.yhiguchi.home_expense.application.service.ExpenseAttributeUpdateService;
import dev.yhiguchi.home_expense.application.service.expense.attribute.ExpenseAttributeSummaryService;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeAlreadyExistsException;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.presentation.validation.ExpenseCategory;
import dev.yhiguchi.home_expense.query.*;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummary;
import java.net.URI;
import java.util.Objects;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@Path("/v1/expense-attributes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseAttributeApi {

  ExpenseAttributeRegistrationService expenseAttributeRegistrationService;
  ExpenseAttributeSummaryService expenseAttributeSummaryService;
  ExpenseAttributeUpdateService expenseAttributeUpdateService;

  ExpenseAttributeDeletionService expenseAttributeDeletionService;

  public ExpenseAttributeApi(
      ExpenseAttributeRegistrationService expenseAttributeRegistrationService,
      ExpenseAttributeSummaryService expenseAttributeSummaryService,
      ExpenseAttributeUpdateService expenseAttributeUpdateService,
      ExpenseAttributeDeletionService expenseAttributeDeletionService) {
    this.expenseAttributeRegistrationService = expenseAttributeRegistrationService;
    this.expenseAttributeSummaryService = expenseAttributeSummaryService;
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
  public Response delete(@PathParam("id") String id, @Valid ExpenseAttributePutRequest request) {
    expenseAttributeDeletionService.delete(new ExpenseAttributeIdentifier(id));
    return Response.noContent().build();
  }

  @GET
  public Response get(
      @RestQuery("category") @ExpenseCategory String category,
      @RestQuery("page") @DefaultValue("1") Integer page,
      @RestQuery("per_page") @DefaultValue("20") Integer perPage) {
    ExpenseAttributeCriteria criteria = toExpenseAttributeCriteria(category, page, perPage);
    ExpenseAttributeSummary expenseAttributeSummary = expenseAttributeSummaryService.find(criteria);
    ExpenseAttributeGetResponse response =
        new ExpenseAttributeGetResponse(criteria, expenseAttributeSummary);
    return Response.ok(response).build();
  }

  ExpenseAttributeCriteria toExpenseAttributeCriteria(
      String category, Integer page, Integer perPage) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    if (Objects.nonNull(category)) {
      return new ExpenseAttributeCriteria(
          dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory.of(category), pagination);
    }
    return new ExpenseAttributeCriteria(pagination);
  }

  @ServerExceptionMapper
  public RestResponse<String> mapException(ExpenseAttributeAlreadyExistsException e) {
    return RestResponse.status(Response.Status.BAD_REQUEST, "既に登録されています");
  }
}
