package dev.yhiguchi.home_expense.config;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.query.expense.ExpenseAggregateCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseAttributeAggregate;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummaryCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummaryCriteria;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(
    targets = {
      ExpenseSummaryCriteria.class,
      ExpenseAttributeSummaryCriteria.class,
      ExpenseAggregateCriteria.class,
      ExpenseAttribute.class,
      Expense.class,
      ExpenseIdentifier.class,
      ExpenseAttributeAggregate.class
    })
public class ReflectionConfig {}
