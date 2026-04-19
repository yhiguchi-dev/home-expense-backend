package dev.yhiguchi.home_expense.presentation.api.expense;

import dev.yhiguchi.home_expense.query.expense.ExpenseStatistics;
import dev.yhiguchi.home_expense.query.expense.ExpenseStatisticsCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseStatisticsQuerier;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/expenses/statistics")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseStatisticsApi {

  ExpenseStatisticsQuerier expenseStatisticsQuerier;

  public ExpenseStatisticsApi(ExpenseStatisticsQuerier expenseStatisticsQuerier) {
    this.expenseStatisticsQuerier = expenseStatisticsQuerier;
  }

  @GET
  @RunOnVirtualThread
  public Response get(
      @QueryParam("year") @Min(value = 1, message = "yearは1以上を指定してください") int year,
      @QueryParam("month")
          @Min(value = 1, message = "monthは1〜12を指定してください")
          @Max(value = 12, message = "monthは1〜12を指定してください")
          int month) {
    ExpenseStatisticsCriteria criteria = new ExpenseStatisticsCriteria(year, month);
    ExpenseStatistics statistics = expenseStatisticsQuerier.find(criteria);
    ExpenseGetStatisticsResponse response = new ExpenseGetStatisticsResponse(statistics);
    return Response.ok(response).build();
  }
}
