package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.IncomeRepository;
import dev.yhiguchi.home_expense.query.income.IncomeSummary;
import dev.yhiguchi.home_expense.query.income.IncomeSummaryCriteria;
import dev.yhiguchi.home_expense.query.income.IncomeSummaryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeGettingService {

  IncomeRepository incomeRepository;
  IncomeSummaryRepository incomeSummaryRepository;

  public IncomeGettingService(
      IncomeRepository incomeRepository, IncomeSummaryRepository incomeSummaryRepository) {
    this.incomeRepository = incomeRepository;
    this.incomeSummaryRepository = incomeSummaryRepository;
  }

  public IncomeSummary findSummary(IncomeSummaryCriteria criteria) {
    return incomeSummaryRepository.find(criteria);
  }

  public Income get(IncomeIdentifier incomeIdentifier) {
    return incomeRepository.get(incomeIdentifier);
  }
}
