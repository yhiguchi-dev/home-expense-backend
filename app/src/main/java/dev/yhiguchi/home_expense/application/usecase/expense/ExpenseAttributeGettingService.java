package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.application.service.expense.attribute.ExpenseAttributeService;
import dev.yhiguchi.home_expense.application.service.expense.attribute.ExpenseAttributeSummaryService;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummary;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummaryCriteria;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
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

  public ExpenseAttribute get(ExpenseAttributeIdentifier expenseIdentifier) {
    return expenseAttributeService.get(expenseIdentifier);
  }
}
