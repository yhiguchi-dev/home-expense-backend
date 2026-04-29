package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.Amount;
import dev.yhiguchi.home_expense.domain.model.Description;
import dev.yhiguchi.home_expense.domain.model.expense.PaymentDate;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;

public record ExpenseRegistrationCommand(
    Description description,
    Amount amount,
    PaymentDate paymentDate,
    ExpenseAttributeIdentifier expenseAttributeIdentifier) {}
