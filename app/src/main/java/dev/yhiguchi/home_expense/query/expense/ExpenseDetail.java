package dev.yhiguchi.home_expense.query.expense;

import java.time.LocalDate;

public record ExpenseDetail(
    String id,
    String description,
    int amount,
    LocalDate paymentDate,
    String attributeId,
    String attributeName,
    String category,
    long version) {}
