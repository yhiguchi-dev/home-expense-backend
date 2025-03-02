package dev.higuchi.homeexpense.quarkus.config;

import dev.higuchi.homeexpense.command.model.expense.ExpenseRepository;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeRepository;
import dev.higuchi.homeexpense.command.model.income.IncomeRepository;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeRepository;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense.*;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense.attribute.ExpenseAttributeDataSource;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense.attribute.ExpenseAttributeMapper;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense.attribute.ExpenseAttributeSummaryDataSource;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense.attribute.ExpenseAttributeSummaryMapper;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income.IncomeDataSource;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income.IncomeMapper;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income.IncomeSummaryDataSource;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income.IncomeSummaryMapper;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income.attribute.IncomeAttributeDataSource;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income.attribute.IncomeAttributeMapper;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income.attribute.IncomeAttributeSummaryDataSource;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income.attribute.IncomeAttributeSummaryMapper;
import dev.higuchi.homeexpense.query.model.expense.ExpenseAggregateRepository;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummaryRepository;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummaryRepository;
import dev.higuchi.homeexpense.query.model.income.IncomeSummaryRepository;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummaryRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class DataSourceDIConfig {
  @Inject ExpenseAttributeMapper expenseAttributeMapper;
  @Inject ExpenseAttributeSummaryMapper expenseAttributeSummaryMapper;
  @Inject ExpenseAggregateMapper expenseAggregateMapper;
  @Inject ExpenseMapper expenseMapper;
  @Inject ExpenseSummaryMapper expenseSummaryMapper;
  @Inject IncomeAttributeMapper incomeAttributeMapper;
  @Inject IncomeAttributeSummaryMapper incomeAttributeSummaryMapper;
  @Inject IncomeMapper incomeMapper;
  @Inject IncomeSummaryMapper incomeSummaryMapper;

  @Singleton
  ExpenseAttributeRepository expenseAttributeRepository() {
    return new ExpenseAttributeDataSource(expenseAttributeMapper);
  }

  @Singleton
  ExpenseAttributeSummaryRepository expenseAttributeSummaryRepository() {
    return new ExpenseAttributeSummaryDataSource(expenseAttributeSummaryMapper);
  }

  @Singleton
  ExpenseAggregateRepository expenseAggregateRepository() {
    return new ExpenseAggregateDataSource(expenseAggregateMapper);
  }

  @Singleton
  ExpenseRepository expenseRepository() {
    return new ExpenseDataSource(expenseMapper);
  }

  @Singleton
  ExpenseSummaryRepository expenseSummaryRepository() {
    return new ExpenseSummaryDataSource(expenseSummaryMapper);
  }

  @Singleton
  IncomeAttributeRepository incomeAttributeRepository() {
    return new IncomeAttributeDataSource(incomeAttributeMapper);
  }

  @Singleton
  IncomeAttributeSummaryRepository incomeAttributeSummaryRepository() {
    return new IncomeAttributeSummaryDataSource(incomeAttributeSummaryMapper);
  }

  @Singleton
  IncomeRepository incomeRepository() {
    return new IncomeDataSource(incomeMapper);
  }

  @Singleton
  IncomeSummaryRepository incomeSummaryRepository() {
    return new IncomeSummaryDataSource(incomeSummaryMapper);
  }
}
