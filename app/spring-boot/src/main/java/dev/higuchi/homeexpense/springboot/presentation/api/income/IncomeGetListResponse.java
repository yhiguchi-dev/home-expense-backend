package dev.higuchi.homeexpense.springboot.presentation.api.income;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.higuchi.homeexpense.query.model.income.IncomeSummary;
import java.util.List;

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
