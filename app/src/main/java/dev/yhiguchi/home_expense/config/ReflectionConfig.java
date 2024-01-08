package dev.yhiguchi.home_expense.config;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeName;
import dev.yhiguchi.home_expense.query.expense.ExpenseAggregateCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseAttributeAggregate;
import dev.yhiguchi.home_expense.query.expense.ExpenseSummaryCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummaryCriteria;
import dev.yhiguchi.home_expense.query.income.IncomeSummaryCriteria;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSummaryCriteria;
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
