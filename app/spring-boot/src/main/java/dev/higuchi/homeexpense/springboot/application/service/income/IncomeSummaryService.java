package dev.higuchi.homeexpense.springboot.application.service.income;

import dev.higuchi.homeexpense.query.model.income.IncomeSummary;
import dev.higuchi.homeexpense.query.model.income.IncomeSummaryCriteria;
import dev.higuchi.homeexpense.query.model.income.IncomeSummaryRepository;
import org.springframework.stereotype.Service;

@Service
public class IncomeSummaryService {

  IncomeSummaryRepository incomeSummaryRepository;

  public IncomeSummaryService(IncomeSummaryRepository incomeSummaryRepository) {
    this.incomeSummaryRepository = incomeSummaryRepository;
  }

  public IncomeSummary find(IncomeSummaryCriteria criteria) {
    return incomeSummaryRepository.find(criteria);
  }
}
