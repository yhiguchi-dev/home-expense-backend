package dev.yhiguchi.home_expense.application.service.income.attribute;

import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSummary;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSummaryCriteria;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSummaryRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IncomeAttributeSummaryService {

  IncomeAttributeSummaryRepository incomeAttributeSummaryRepository;

  public IncomeAttributeSummaryService(
      IncomeAttributeSummaryRepository incomeAttributeSummaryRepository) {
    this.incomeAttributeSummaryRepository = incomeAttributeSummaryRepository;
  }

  public IncomeAttributeSummary find(IncomeAttributeSummaryCriteria criteria) {
    return incomeAttributeSummaryRepository.find(criteria);
  }
}
