package dev.higuchi.homeexpense.quarkus.application.usecase.expense;

import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.quarkus.application.service.expense.attribute.ExpenseAttributeService;
import dev.higuchi.homeexpense.quarkus.application.service.expense.attribute.ExpenseAttributeSummaryService;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummary;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummaryCriteria;
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
