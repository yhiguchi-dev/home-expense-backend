package dev.yhiguchi.home_expense.presentation.api.income;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yhiguchi.home_expense.query.income.IncomeSummary;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
class IncomeGetListResponse {
  @JsonProperty("incomes")
  List<IncomeGetResponse> list;

  IncomeGetListResponse(IncomeSummary summary) {
    this.list = summary.list().stream().map(IncomeGetResponse::from).toList();
  }

  IncomeGetListResponse() {
    this.list = List.of();
  }
}
