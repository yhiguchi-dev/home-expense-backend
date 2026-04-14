package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseAttributeUpdateService {

  ExpenseAttributeRepository expenseAttributeRepository;

  public ExpenseAttributeUpdateService(ExpenseAttributeRepository expenseAttributeRepository) {
    this.expenseAttributeRepository = expenseAttributeRepository;
  }

  public void update(
      ExpenseAttributeIdentifier expenseAttributeIdentifier,
      ExpenseAttributeName expenseAttributeName,
      ExpenseCategory expenseCategory,
      long version) {
    ExpenseAttribute attribute = expenseAttributeRepository.get(expenseAttributeIdentifier);
    ExpenseAttribute updated = attribute.updateWith(expenseAttributeName, expenseCategory);
    if (attribute.hasChanges(updated)) {
      expenseAttributeRepository.update(updated.withVersion(version));
    }
  }
}
