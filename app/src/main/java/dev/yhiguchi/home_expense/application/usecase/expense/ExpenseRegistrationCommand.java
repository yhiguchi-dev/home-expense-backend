package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Description;
import dev.yhiguchi.home_expense.domain.model.expense.PaymentDate;
import dev.yhiguchi.home_expense.domain.model.expense.Price;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;

public record ExpenseRegistrationCommand(
    Description description,
    Price price,
    PaymentDate paymentDate,
    ExpenseAttributeIdentifier expenseAttributeIdentifier) {}
