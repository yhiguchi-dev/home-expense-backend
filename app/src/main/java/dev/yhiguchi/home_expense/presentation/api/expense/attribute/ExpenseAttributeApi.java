package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseAttributeDeletionService;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseAttributeRegistrationService;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseAttributeRetrievalService;
import dev.yhiguchi.home_expense.application.usecase.expense.ExpenseAttributeUpdateService;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.presentation.api.LinkHeaderCreatable;
import dev.yhiguchi.home_expense.presentation.validation.ExpenseCategory;
import dev.yhiguchi.home_expense.query.*;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSearchCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSearchResult;
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
import java.util.Objects;

@Path("/v1/expense-attributes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseAttributeApi implements LinkHeaderCreatable {

  ExpenseAttributeRegistrationService expenseAttributeRegistrationService;
  ExpenseAttributeRetrievalService expenseAttributeRetrievalService;
  ExpenseAttributeUpdateService expenseAttributeUpdateService;

  ExpenseAttributeDeletionService expenseAttributeDeletionService;

  public ExpenseAttributeApi(
      ExpenseAttributeRegistrationService expenseAttributeRegistrationService,
      ExpenseAttributeRetrievalService expenseAttributeRetrievalService,
      ExpenseAttributeUpdateService expenseAttributeUpdateService,
      ExpenseAttributeDeletionService expenseAttributeDeletionService) {
    this.expenseAttributeRegistrationService = expenseAttributeRegistrationService;
    this.expenseAttributeRetrievalService = expenseAttributeRetrievalService;
    this.expenseAttributeUpdateService = expenseAttributeUpdateService;
    this.expenseAttributeDeletionService = expenseAttributeDeletionService;
  }

  @POST
  @RunOnVirtualThread
  public Response post(@Valid ExpenseAttributePostRequest request, @Context UriInfo uriInfo) {
    ExpenseAttributeIdentifier expenseAttributeIdentifier =
        expenseAttributeRegistrationService.createAndRegister(
            request.toExpenseAttributeName(), request.toExpenseCategory());
    URI uri = uriInfo.getAbsolutePathBuilder().path(expenseAttributeIdentifier.value()).build();
    return Response.created(uri).build();
  }

  @PUT
  @Path("{id}")
  @RunOnVirtualThread
  public Response put(
      @PathParam("id") @Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "IDの形式が不正です") String id,
      @Valid ExpenseAttributePutRequest request) {
    expenseAttributeUpdateService.update(
        new ExpenseAttributeIdentifier(id),
        request.toExpenseAttributeName(),
        request.toExpenseCategory());
    return Response.noContent().build();
  }

  @DELETE
  @Path("{id}")
  @RunOnVirtualThread
  public Response delete(
      @PathParam("id") @Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "IDの形式が不正です") String id) {
    expenseAttributeDeletionService.delete(new ExpenseAttributeIdentifier(id));
    return Response.noContent().build();
  }

  @GET
  @RunOnVirtualThread
  public Response get(
      @QueryParam("category") @ExpenseCategory String category,
      @QueryParam("page") @DefaultValue("1") @Min(value = 1, message = "pageは1以上を指定してください")
          Integer page,
      @QueryParam("per_page")
          @DefaultValue("20")
          @Min(value = 1, message = "per_pageは1以上を指定してください")
          @jakarta.validation.constraints.Max(value = 100, message = "per_pageは100以下を指定してください")
          Integer perPage,
      @Context UriInfo uriInfo) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    ExpenseAttributeSearchCriteria criteria =
        Objects.nonNull(category)
            ? new ExpenseAttributeSearchCriteria(
                dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory.of(category),
                pagination)
            : new ExpenseAttributeSearchCriteria(pagination);
    ExpenseAttributeSearchResult expenseAttributeSearchResult =
        expenseAttributeRetrievalService.search(criteria);
    ExpenseAttributeGetListResponse response =
        page <= expenseAttributeSearchResult.totalCount()
            ? new ExpenseAttributeGetListResponse(expenseAttributeSearchResult)
            : new ExpenseAttributeGetListResponse();
    Response.ResponseBuilder responseBuilder = Response.ok(response);
    responseBuilder.header(
        "Link", create(uriInfo, pagination, expenseAttributeSearchResult.totalCount()));
    return responseBuilder.build();
  }

  @GET
  @Path("{id}")
  @RunOnVirtualThread
  public Response get(
      @PathParam("id") @Pattern(regexp = "^[a-f0-9\\-]{36}$", message = "IDの形式が不正です") String id) {
    ExpenseAttribute expenseAttribute =
        expenseAttributeRetrievalService.get(new ExpenseAttributeIdentifier(id));
    ExpenseAttributeGetResponse response = ExpenseAttributeGetResponse.from(expenseAttribute);
    return Response.ok(response).build();
  }
}
