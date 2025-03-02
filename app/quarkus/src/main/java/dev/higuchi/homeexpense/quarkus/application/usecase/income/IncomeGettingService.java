package dev.higuchi.homeexpense.quarkus.application.usecase.income;

import dev.higuchi.homeexpense.command.model.income.Income;
import dev.higuchi.homeexpense.command.model.income.IncomeIdentifier;
import dev.higuchi.homeexpense.quarkus.application.service.income.IncomeService;
import dev.higuchi.homeexpense.quarkus.application.service.income.IncomeSummaryService;
import dev.higuchi.homeexpense.query.model.income.IncomeSummary;
import dev.higuchi.homeexpense.query.model.income.IncomeSummaryCriteria;
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
