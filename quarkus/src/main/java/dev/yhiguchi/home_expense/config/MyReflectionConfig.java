package dev.yhiguchi.home_expense.config;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.query.expense.ExpenseCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeCriteria;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(
    targets = {
      ExpenseCriteria.class,
      ExpenseAttributeCriteria.class,
      ExpenseAttribute.class,
      Expense.class,
      ExpenseIdentifier.class
    })
public class MyReflectionConfig {}
