package dev.yhiguchi.home_expense.presentation.problem;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseNotFoundException;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeAlreadyExistsException;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeConstraintException;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeNotFoundException;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeAlreadyExistsException;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeConstraintException;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeNotFoundException;
import dev.yhiguchi.home_expense.infrastructure.datasource.ConcurrentUpdateException;
import dev.yhiguchi.home_expense.infrastructure.datasource.DataAccessException;
import dev.yhiguchi.home_expense.presentation.validation.payload.PreconditionRequired;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

/** RFC 9457 Problem Details を返すグローバル例外マッパー */
public class ProblemDetailExceptionMapper {

  private static final Logger LOG = Logger.getLogger(ProblemDetailExceptionMapper.class);
  private static final String PROBLEM_JSON = "application/problem+json";

  @Context UriInfo uriInfo;

  @ServerExceptionMapper
  public RestResponse<ProblemDetail> mapExpenseNotFoundException(ExpenseNotFoundException e) {
    return toResponse(Response.Status.NOT_FOUND, "経費が見つかりません");
  }

  @ServerExceptionMapper
  public RestResponse<ProblemDetail> mapExpenseAttributeNotFoundException(
      ExpenseAttributeNotFoundException e) {
    return toResponse(Response.Status.NOT_FOUND, "経費属性が見つかりません");
  }

  @ServerExceptionMapper
  public RestResponse<ProblemDetail> mapIncomeNotFoundException(
      dev.yhiguchi.home_expense.domain.model.income.IncomeNotFoundException e) {
    return toResponse(Response.Status.NOT_FOUND, "収入が見つかりません");
  }

  @ServerExceptionMapper
  public RestResponse<ProblemDetail> mapIncomeAttributeNotFoundException(
      IncomeAttributeNotFoundException e) {
    return toResponse(Response.Status.NOT_FOUND, "収入属性が見つかりません");
  }

  @ServerExceptionMapper
  public RestResponse<ProblemDetail> mapExpenseAttributeAlreadyExistsException(
      ExpenseAttributeAlreadyExistsException e) {
    return toResponse(Response.Status.CONFLICT, "既に登録されています");
  }

  @ServerExceptionMapper
  public RestResponse<ProblemDetail> mapIncomeAttributeAlreadyExistsException(
      IncomeAttributeAlreadyExistsException e) {
    return toResponse(Response.Status.CONFLICT, "既に登録されています");
  }

  @ServerExceptionMapper
  public RestResponse<ProblemDetail> mapExpenseAttributeConstraintException(
      ExpenseAttributeConstraintException e) {
    return toResponse(Response.Status.CONFLICT, "制約があるため削除できません");
  }

  @ServerExceptionMapper
  public RestResponse<ProblemDetail> mapIncomeAttributeConstraintException(
      IncomeAttributeConstraintException e) {
    return toResponse(Response.Status.CONFLICT, "制約があるため削除できません");
  }

  @ServerExceptionMapper
  public RestResponse<ProblemDetail> mapConstraintViolationException(
      ConstraintViolationException e) {
    var violations = e.getConstraintViolations();

    var preconditionRequired =
        violations.stream()
            .filter(
                v -> v.getConstraintDescriptor().getPayload().contains(PreconditionRequired.class))
            .findFirst();
    if (preconditionRequired.isPresent()) {
      return toResponse(428, "Precondition Required", preconditionRequired.get().getMessage());
    }

    String detail =
        violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
    return toResponse(Response.Status.BAD_REQUEST, detail);
  }

  @ServerExceptionMapper
  public RestResponse<ProblemDetail> mapConcurrentUpdateException(ConcurrentUpdateException e) {
    LOG.warnv("楽観ロック違反: {0}", uriInfo.getRequestUri());
    return toResponse(
        Response.Status.PRECONDITION_FAILED, "指定されたバージョンは既に更新されています。最新を取得して再試行してください");
  }

  @ServerExceptionMapper
  public RestResponse<ProblemDetail> mapDataAccessException(DataAccessException e) {
    LOG.error("データアクセスエラー", e);
    return toResponse(Response.Status.INTERNAL_SERVER_ERROR, "データアクセスエラーが発生しました");
  }

  private RestResponse<ProblemDetail> toResponse(Response.Status status, String detail) {
    return toResponse(status.getStatusCode(), status.getReasonPhrase(), detail);
  }

  private RestResponse<ProblemDetail> toResponse(int statusCode, String title, String detail) {
    URI instance = uriInfo.getRequestUri();
    ProblemDetail problemDetail = ProblemDetail.of(statusCode, title, detail, instance);
    return RestResponse.ResponseBuilder.create(
            RestResponse.Status.fromStatusCode(statusCode), problemDetail)
        .header("Content-Type", PROBLEM_JSON)
        .build();
  }
}
