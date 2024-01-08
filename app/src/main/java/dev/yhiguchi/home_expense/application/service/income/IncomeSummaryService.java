package dev.yhiguchi.home_expense.application.service.income;

import dev.yhiguchi.home_expense.query.income.IncomeSummary;
import dev.yhiguchi.home_expense.query.income.IncomeSummaryCriteria;
import dev.yhiguchi.home_expense.query.income.IncomeSummaryRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IncomeSummaryService {

  IncomeSummaryRepository incomeSummaryRepository;

  public IncomeSummaryService(IncomeSummaryRepository incomeSummaryRepository) {
    this.incomeSummaryRepository = incomeSummaryRepository;
  }

  public IncomeSummary find(IncomeSummaryCriteria criteria) {
    return incomeSummaryRepository.find(criteria);
  }
}
