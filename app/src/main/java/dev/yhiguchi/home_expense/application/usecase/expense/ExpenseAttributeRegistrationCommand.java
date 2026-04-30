package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;

public record ExpenseAttributeRegistrationCommand(
    ExpenseAttributeName expenseAttributeName, ExpenseCategory expenseCategory) {}
