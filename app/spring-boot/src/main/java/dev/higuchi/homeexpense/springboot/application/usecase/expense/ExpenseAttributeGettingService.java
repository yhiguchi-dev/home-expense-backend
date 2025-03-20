package dev.higuchi.homeexpense.springboot.application.usecase.expense;

import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummary;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummaryCriteria;
import dev.higuchi.homeexpense.springboot.application.service.expense.attribute.ExpenseAttributeService;
import dev.higuchi.homeexpense.springboot.application.service.expense.attribute.ExpenseAttributeSummaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseAttributeGettingService {

  ExpenseAttributeService expenseAttributeService;
  ExpenseAttributeSummaryService expenseAttributeSummaryService;

  public ExpenseAttributeGettingService(
      ExpenseAttributeService expenseAttributeService,
      ExpenseAttributeSummaryService expenseAttributeSummaryService) {
    this.expenseAttributeService = expenseAttributeService;
    this.expenseAttributeSummaryService = expenseAttributeSummaryService;
  }

  public ExpenseAttributeSummary findSummary(ExpenseAttributeSummaryCriteria criteria) {
    return expenseAttributeSummaryService.find(criteria);
  }

  @Transactional(readOnly = true)
  public ExpenseAttribute get(ExpenseAttributeIdentifier expenseIdentifier) {
    return expenseAttributeService.get(expenseIdentifier);
  }
}
