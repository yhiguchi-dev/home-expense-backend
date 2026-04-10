package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeRepository;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummary;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummaryCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummaryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseAttributeGettingService {

  ExpenseAttributeRepository expenseAttributeRepository;
  ExpenseAttributeSummaryRepository expenseAttributeSummaryRepository;

  public ExpenseAttributeGettingService(
      ExpenseAttributeRepository expenseAttributeRepository,
      ExpenseAttributeSummaryRepository expenseAttributeSummaryRepository) {
    this.expenseAttributeRepository = expenseAttributeRepository;
    this.expenseAttributeSummaryRepository = expenseAttributeSummaryRepository;
  }

  public ExpenseAttributeSummary findSummary(ExpenseAttributeSummaryCriteria criteria) {
    return expenseAttributeSummaryRepository.find(criteria);
  }

  public ExpenseAttribute get(ExpenseAttributeIdentifier expenseIdentifier) {
    return expenseAttributeRepository.get(expenseIdentifier);
  }
}
