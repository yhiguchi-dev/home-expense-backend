package dev.higuchi.homeexpense.springboot.application.usecase.income;

import dev.higuchi.homeexpense.command.model.income.Income;
import dev.higuchi.homeexpense.command.model.income.IncomeIdentifier;
import dev.higuchi.homeexpense.query.model.income.IncomeSummary;
import dev.higuchi.homeexpense.query.model.income.IncomeSummaryCriteria;
import dev.higuchi.homeexpense.springboot.application.service.income.IncomeService;
import dev.higuchi.homeexpense.springboot.application.service.income.IncomeSummaryService;
import org.springframework.stereotype.Service;

@Service
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
