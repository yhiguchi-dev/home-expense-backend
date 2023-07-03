package dev.yhiguchi.home_expense.config;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.query.expense.ExpenseCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeCriteria;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(
    targets = {ExpenseCriteria.class, ExpenseAttributeCriteria.class, ExpenseAttribute.class})
public class MyReflectionConfig {}
