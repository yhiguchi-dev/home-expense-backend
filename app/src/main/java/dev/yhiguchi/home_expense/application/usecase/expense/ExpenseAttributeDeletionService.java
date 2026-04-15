package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseRepository;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseAttributeDeletionService {

  ExpenseAttributeRepository expenseAttributeRepository;
  ExpenseAttributeDeletionPolicy expenseAttributeDeletionPolicy;

  public ExpenseAttributeDeletionService(
      ExpenseAttributeRepository expenseAttributeRepository,
      ExpenseRepository expenseRepository) {
    this.expenseAttributeRepository = expenseAttributeRepository;
    this.expenseAttributeDeletionPolicy =
        new ExpenseAttributeDeletionPolicy(expenseRepository::find);
  }

  public void delete(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    ExpenseAttribute attribute = expenseAttributeRepository.get(expenseAttributeIdentifier);
    expenseAttributeDeletionPolicy.assertDeletable(attribute);
    expenseAttributeRepository.delete(attribute);
  }
}
