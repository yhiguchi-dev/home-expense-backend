package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.application.service.income.attribute.IncomeAttributeService;
import dev.yhiguchi.home_expense.application.service.income.attribute.IncomeAttributeSummaryService;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSummary;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSummaryCriteria;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeAttributeGettingService {

  IncomeAttributeService incomeAttributeService;
  IncomeAttributeSummaryService incomeAttributeSummaryService;

  public IncomeAttributeGettingService(
      IncomeAttributeService incomeAttributeService,
      IncomeAttributeSummaryService incomeAttributeSummaryService) {
    this.incomeAttributeService = incomeAttributeService;
    this.incomeAttributeSummaryService = incomeAttributeSummaryService;
  }

  public IncomeAttributeSummary findSummary(IncomeAttributeSummaryCriteria criteria) {
    return incomeAttributeSummaryService.find(criteria);
  }

  public IncomeAttribute get(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return incomeAttributeService.get(incomeAttributeIdentifier);
  }
}
