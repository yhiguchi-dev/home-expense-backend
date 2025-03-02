package dev.higuchi.homeexpense.springboot.configuration;

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
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource")
public class DataSourceDIConfig {

  @Bean
  ExpenseAttributeRepository expenseAttributeRepository(
      ExpenseAttributeMapper expenseAttributeMapper) {
    return new ExpenseAttributeDataSource(expenseAttributeMapper);
  }

  @Bean
  ExpenseAttributeSummaryRepository expenseAttributeSummaryRepository(
      ExpenseAttributeSummaryMapper expenseAttributeSummaryMapper) {
    return new ExpenseAttributeSummaryDataSource(expenseAttributeSummaryMapper);
  }

  @Bean
  ExpenseAggregateRepository expenseAggregateRepository(
      ExpenseAggregateMapper expenseAggregateMapper) {
    return new ExpenseAggregateDataSource(expenseAggregateMapper);
  }

  @Bean
  ExpenseRepository expenseRepository(ExpenseMapper expenseMapper) {
    return new ExpenseDataSource(expenseMapper);
  }

  @Bean
  ExpenseSummaryRepository expenseSummaryRepository(ExpenseSummaryMapper expenseSummaryMapper) {
    return new ExpenseSummaryDataSource(expenseSummaryMapper);
  }

  @Bean
  IncomeAttributeRepository incomeAttributeRepository(IncomeAttributeMapper incomeAttributeMapper) {
    return new IncomeAttributeDataSource(incomeAttributeMapper);
  }

  @Bean
  IncomeAttributeSummaryRepository incomeAttributeSummaryRepository(
      IncomeAttributeSummaryMapper incomeAttributeSummaryMapper) {
    return new IncomeAttributeSummaryDataSource(incomeAttributeSummaryMapper);
  }

  @Bean
  IncomeRepository incomeRepository(IncomeMapper incomeMapper) {
    return new IncomeDataSource(incomeMapper);
  }

  @Bean
  IncomeSummaryRepository incomeSummaryRepository(IncomeSummaryMapper incomeSummaryMapper) {
    return new IncomeSummaryDataSource(incomeSummaryMapper);
  }
}
