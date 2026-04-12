package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.query.expense.ExpenseStatistics;
import dev.yhiguchi.home_expense.query.expense.ExpenseStatisticsCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseStatisticsQuerier;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseStatisticsService {

  ExpenseStatisticsQuerier expenseStatisticsQuerier;

  public ExpenseStatisticsService(ExpenseStatisticsQuerier expenseStatisticsQuerier) {
    this.expenseStatisticsQuerier = expenseStatisticsQuerier;
  }

  public ExpenseStatistics findStatistics(ExpenseStatisticsCriteria criteria) {
    return expenseStatisticsQuerier.find(criteria);
  }
}
