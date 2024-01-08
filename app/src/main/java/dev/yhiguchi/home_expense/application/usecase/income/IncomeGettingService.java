package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.application.service.income.IncomeService;
import dev.yhiguchi.home_expense.application.service.income.IncomeSummaryService;
import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.query.income.IncomeSummary;
import dev.yhiguchi.home_expense.query.income.IncomeSummaryCriteria;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeGettingService {

  IncomeService incomeService;
  IncomeSummaryService incomeSummaryService;

  public IncomeGettingService(
      IncomeService incomeService, IncomeSummaryService incomeSummaryService) {
    this.incomeService = incomeService;
    this.incomeSummaryService = incomeSummaryService;
  }

  public IncomeSummary findSummary(IncomeSummaryCriteria criteria) {
    return incomeSummaryService.find(criteria);
  }

  public Income get(IncomeIdentifier incomeIdentifier) {
    return incomeService.get(incomeIdentifier);
  }
}
