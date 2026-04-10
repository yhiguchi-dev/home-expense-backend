package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeRepository;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSummary;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSummaryCriteria;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSummaryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeAttributeGettingService {

  IncomeAttributeRepository incomeAttributeRepository;
  IncomeAttributeSummaryRepository incomeAttributeSummaryRepository;

  public IncomeAttributeGettingService(
      IncomeAttributeRepository incomeAttributeRepository,
      IncomeAttributeSummaryRepository incomeAttributeSummaryRepository) {
    this.incomeAttributeRepository = incomeAttributeRepository;
    this.incomeAttributeSummaryRepository = incomeAttributeSummaryRepository;
  }

  public IncomeAttributeSummary findSummary(IncomeAttributeSummaryCriteria criteria) {
    return incomeAttributeSummaryRepository.find(criteria);
  }

  public IncomeAttribute get(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return incomeAttributeRepository.get(incomeAttributeIdentifier);
  }
}
