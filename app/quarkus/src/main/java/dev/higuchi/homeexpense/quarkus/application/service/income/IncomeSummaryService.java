package dev.higuchi.homeexpense.quarkus.application.service.income;

import dev.higuchi.homeexpense.query.model.income.IncomeSummary;
import dev.higuchi.homeexpense.query.model.income.IncomeSummaryCriteria;
import dev.higuchi.homeexpense.query.model.income.IncomeSummaryRepository;
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
