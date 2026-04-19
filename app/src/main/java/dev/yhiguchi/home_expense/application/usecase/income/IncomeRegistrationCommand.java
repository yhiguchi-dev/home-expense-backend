package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.Amount;
import dev.yhiguchi.home_expense.domain.model.income.Description;
import dev.yhiguchi.home_expense.domain.model.income.ReceiveDate;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;

public record IncomeRegistrationCommand(
    Description description,
    Amount amount,
    ReceiveDate receiveDate,
    IncomeAttributeIdentifier incomeAttributeIdentifier) {}
