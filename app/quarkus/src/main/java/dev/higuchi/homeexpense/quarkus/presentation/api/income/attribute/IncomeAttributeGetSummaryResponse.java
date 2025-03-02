package dev.higuchi.homeexpense.quarkus.presentation.api.income.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummary;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
class IncomeAttributeGetSummaryResponse {

  @JsonProperty("income_attributes")
  List<IncomeAttributeGetResponse> list;

  IncomeAttributeGetSummaryResponse(IncomeAttributeSummary summary) {
    this.list = summary.list().stream().map(IncomeAttributeGetResponse::from).toList();
  }

  IncomeAttributeGetSummaryResponse() {
    this.list = List.of();
  }
}
