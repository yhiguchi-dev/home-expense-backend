package dev.higuchi.homeexpense.springboot.application.usecase.income;

import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeIdentifier;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummary;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummaryCriteria;
import dev.higuchi.homeexpense.springboot.application.service.income.attribute.IncomeAttributeService;
import dev.higuchi.homeexpense.springboot.application.service.income.attribute.IncomeAttributeSummaryService;
import org.springframework.stereotype.Service;

@Service
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
