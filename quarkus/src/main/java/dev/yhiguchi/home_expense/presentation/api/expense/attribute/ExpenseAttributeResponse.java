package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;

record ExpenseAttributeResponse(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("category") String category) {}
