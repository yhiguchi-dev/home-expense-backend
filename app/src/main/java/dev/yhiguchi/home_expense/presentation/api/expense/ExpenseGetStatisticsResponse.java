package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.expense.ExpenseStatistics;

class ExpenseGetStatisticsResponse {

  @JsonProperty("income_total_amount")
  Long incomeTotalAmount;

  @JsonProperty("disposal_income_amount")
  Long disposalIncomeAmount;

  @JsonProperty("total_amount")
  Long totalAmount;

  @JsonProperty("fixed_expense_detail")
  ExpenseStatisticsDetailResponse fixedDetail;

  @JsonProperty("variable_expense_detail")
  ExpenseStatisticsDetailResponse variableDetail;

  ExpenseGetStatisticsResponse(ExpenseStatistics statistics) {
    this.incomeTotalAmount = statistics.incomeTotalAmount();
    this.disposalIncomeAmount = statistics.disposableIncome();
    this.totalAmount = statistics.totalAmount();
    this.fixedDetail = new ExpenseStatisticsDetailResponse(statistics.fixedDetail());
    this.variableDetail = new ExpenseStatisticsDetailResponse(statistics.variableDetail());
  }
}
