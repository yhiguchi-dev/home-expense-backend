package dev.higuchi.homeexpense.quarkus.application.service.income.attribute;

import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummary;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummaryCriteria;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummaryRepository;
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
