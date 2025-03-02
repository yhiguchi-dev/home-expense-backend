package dev.higuchi.homeexpense.quarkus.config;

import dev.higuchi.homeexpense.command.model.expense.Expense;
import dev.higuchi.homeexpense.command.model.expense.ExpenseIdentifier;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeName;
import dev.higuchi.homeexpense.command.model.income.Income;
import dev.higuchi.homeexpense.command.model.income.IncomeIdentifier;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeIdentifier;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeName;
import dev.higuchi.homeexpense.query.model.expense.*;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummaryCriteria;
import dev.higuchi.homeexpense.query.model.income.IncomeSummaryCriteria;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummaryCriteria;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(
    targets = {
      ExpenseSummaryCriteria.class,
      ExpenseAttributeSummaryCriteria.class,
      ExpenseAggregateCriteria.class,
      ExpenseAttribute.class,
      Expense.class,
      ExpenseIdentifier.class,
      ExpenseAttributeIdentifier.class,
      ExpenseAttributeName.class,
      ExpenseAttributeAggregate.class,
      IncomeSummaryCriteria.class,
      IncomeAttributeSummaryCriteria.class,
      IncomeAttribute.class,
      Income.class,
      IncomeIdentifier.class,
      IncomeAttributeIdentifier.class,
      IncomeAttributeName.class
    })
public class ReflectionConfig {}
