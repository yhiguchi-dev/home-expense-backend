package dev.yhiguchi.home_expense.presentation.api.expense;

import com.fasterxml.jackson.annotation.JsonProperty;

record ExpenseResponse(
    @JsonProperty("id") String id,
    @JsonProperty("description") String description,
    @JsonProperty("price") Integer price,
    @JsonProperty("payment_date") String paymentDate,
    @JsonProperty("expense_attribute") ExpenseAttributeResponse expenseAttributeResponse) {}
