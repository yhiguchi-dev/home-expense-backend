package dev.higuchi.homeexpense.springboot.configuration;

import dev.higuchi.homeexpense.command.model.expense.ExpenseRepository;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeRepository;
import dev.higuchi.homeexpense.command.model.income.IncomeRepository;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeRepository;
import dev.higuchi.homeexpense.query.model.expense.ExpenseAggregateRepository;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummaryRepository;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummaryRepository;
import dev.higuchi.homeexpense.query.model.income.IncomeSummaryRepository;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummaryRepository;
import dev.higuchi.homeexpense.springboot.application.service.expense.ExpenseAggregateService;
import dev.higuchi.homeexpense.springboot.application.service.expense.ExpenseService;
import dev.higuchi.homeexpense.springboot.application.service.expense.ExpenseSummaryService;
import dev.higuchi.homeexpense.springboot.application.service.expense.attribute.ExpenseAttributeService;
import dev.higuchi.homeexpense.springboot.application.service.expense.attribute.ExpenseAttributeSummaryService;
import dev.higuchi.homeexpense.springboot.application.service.income.IncomeService;
import dev.higuchi.homeexpense.springboot.application.service.income.IncomeSummaryService;
import dev.higuchi.homeexpense.springboot.application.service.income.attribute.IncomeAttributeService;
import dev.higuchi.homeexpense.springboot.application.service.income.attribute.IncomeAttributeSummaryService;
import dev.higuchi.homeexpense.springboot.application.usecase.expense.*;
import dev.higuchi.homeexpense.springboot.application.usecase.income.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceDIConfig {

  @Bean
  ExpenseAttributeService expenseAttributeService(
      ExpenseAttributeRepository expenseAttributeRepository) {
    return new ExpenseAttributeService(expenseAttributeRepository);
  }

  @Bean
  ExpenseAttributeSummaryService expenseAttributeSummaryService(
      ExpenseAttributeSummaryRepository expenseAttributeSummaryRepository) {
    return new ExpenseAttributeSummaryService(expenseAttributeSummaryRepository);
  }

  @Bean
  ExpenseAggregateService expenseAggregateService(
      ExpenseAggregateRepository expenseAggregateRepository) {
    return new ExpenseAggregateService(expenseAggregateRepository);
  }

  @Bean
  ExpenseService expenseService(ExpenseRepository expenseRepository) {
    return new ExpenseService(expenseRepository);
  }

  @Bean
  ExpenseSummaryService expenseSummaryService(ExpenseSummaryRepository expenseSummaryRepository) {
    return new ExpenseSummaryService(expenseSummaryRepository);
  }

  @Bean
  IncomeAttributeService incomeAttributeService(
      IncomeAttributeRepository incomeAttributeRepository) {
    return new IncomeAttributeService(incomeAttributeRepository);
  }

  @Bean
  IncomeAttributeSummaryService incomeAttributeSummaryService(
      IncomeAttributeSummaryRepository incomeAttributeSummaryRepository) {
    return new IncomeAttributeSummaryService(incomeAttributeSummaryRepository);
  }

  @Bean
  IncomeService incomeService(IncomeRepository incomeRepository) {
    return new IncomeService(incomeRepository);
  }

  @Bean
  IncomeSummaryService incomeSummaryService(IncomeSummaryRepository incomeSummaryRepository) {
    return new IncomeSummaryService(incomeSummaryRepository);
  }

  // use case

  @Bean
  ExpenseAttributeDeletionService expenseAttributeDeletionService(
      ExpenseAttributeService expenseAttributeService, ExpenseService expenseService) {
    return new ExpenseAttributeDeletionService(expenseAttributeService, expenseService);
  }

  @Bean
  ExpenseAttributeGettingService expenseAttributeGettingService(
      ExpenseAttributeService expenseAttributeService,
      ExpenseAttributeSummaryService expenseAttributeSummaryService) {
    return new ExpenseAttributeGettingService(
        expenseAttributeService, expenseAttributeSummaryService);
  }

  @Bean
  ExpenseAttributeRegistrationService expenseAttributeRegistrationService(
      ExpenseAttributeService expenseAttributeService) {
    return new ExpenseAttributeRegistrationService(expenseAttributeService);
  }

  @Bean
  ExpenseAttributeUpdateService expenseAttributeUpdateService(
      ExpenseAttributeService expenseAttributeService) {
    return new ExpenseAttributeUpdateService(expenseAttributeService);
  }

  @Bean
  ExpenseDeletionService expenseDeletionService(
      ExpenseService expenseService, ExpenseAttributeService expenseAttributeService) {
    return new ExpenseDeletionService(expenseService, expenseAttributeService);
  }

  @Bean
  ExpenseGettingService expenseGettingService(
      ExpenseService expenseService,
      ExpenseSummaryService expenseSummaryService,
      ExpenseAggregateService expenseAggregateService) {
    return new ExpenseGettingService(
        expenseService, expenseSummaryService, expenseAggregateService);
  }

  @Bean
  ExpenseRegistrationService expenseRegistrationService(
      ExpenseService expenseService, ExpenseAttributeService expenseAttributeService) {
    return new ExpenseRegistrationService(expenseService, expenseAttributeService);
  }

  @Bean
  ExpenseUpdateService expenseUpdateService(
      ExpenseService expenseService, ExpenseAttributeService expenseAttributeService) {
    return new ExpenseUpdateService(expenseService, expenseAttributeService);
  }

  @Bean
  IncomeAttributeDeletionService incomeAttributeDeletionService(
      IncomeAttributeService incomeAttributeService, IncomeService incomeService) {
    return new IncomeAttributeDeletionService(incomeAttributeService, incomeService);
  }

  @Bean
  IncomeAttributeGettingService incomeAttributeGettingService(
      IncomeAttributeService incomeAttributeService,
      IncomeAttributeSummaryService incomeAttributeSummaryService) {
    return new IncomeAttributeGettingService(incomeAttributeService, incomeAttributeSummaryService);
  }

  @Bean
  IncomeAttributeRegistrationService incomeAttributeRegistrationService(
      IncomeAttributeService incomeAttributeService) {
    return new IncomeAttributeRegistrationService(incomeAttributeService);
  }

  @Bean
  IncomeAttributeUpdateService incomeAttributeUpdateService(
      IncomeAttributeService incomeAttributeService) {
    return new IncomeAttributeUpdateService(incomeAttributeService);
  }

  @Bean
  IncomeDeletionService incomeDeletionService(
      IncomeService incomeService, IncomeAttributeService incomeAttributeService) {
    return new IncomeDeletionService(incomeService, incomeAttributeService);
  }

  @Bean
  IncomeGettingService incomeGettingService(
      IncomeService incomeService, IncomeSummaryService incomeSummaryService) {
    return new IncomeGettingService(incomeService, incomeSummaryService);
  }

  @Bean
  IncomeRegistrationService incomeRegistrationService(
      IncomeService incomeService, IncomeAttributeService incomeAttributeService) {
    return new IncomeRegistrationService(incomeService, incomeAttributeService);
  }

  @Bean
  IncomeUpdateService incomeUpdateService(
      IncomeService incomeService, IncomeAttributeService incomeAttributeService) {
    return new IncomeUpdateService(incomeService, incomeAttributeService);
  }
}
