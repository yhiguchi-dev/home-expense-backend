package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;

public record ExpenseAttributeUpdateCommand(
    ExpenseAttributeIdentifier expenseAttributeIdentifier,
    ExpenseAttributeName expenseAttributeName,
    ExpenseCategory expenseCategory,
    long version) {}
