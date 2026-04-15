package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.ExpenseStatistics;

public record ExpenseGetStatisticsResponse(
    @JsonProperty("income_total_amount") Long incomeTotalAmount,
    @JsonProperty("disposal_income_amount") Long disposalIncomeAmount,
    @JsonProperty("total_amount") Long totalAmount,
    @JsonProperty("fixed_expense_detail") ExpenseStatisticsDetailResponse fixedDetail,
    @JsonProperty("variable_expense_detail") ExpenseStatisticsDetailResponse variableDetail) {

  ExpenseGetStatisticsResponse(ExpenseStatistics statistics) {
    this(
        statistics.incomeTotalAmount(),
        statistics.disposableIncome(),
        statistics.totalAmount(),
        new ExpenseStatisticsDetailResponse(statistics.fixedDetail()),
        new ExpenseStatisticsDetailResponse(statistics.variableDetail()));
  }
}
