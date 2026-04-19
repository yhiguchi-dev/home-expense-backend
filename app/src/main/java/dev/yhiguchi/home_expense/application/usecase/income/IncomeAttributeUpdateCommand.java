package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeName;

public record IncomeAttributeUpdateCommand(
    IncomeAttributeIdentifier incomeAttributeIdentifier,
    IncomeAttributeName incomeAttributeName,
    long version) {}
