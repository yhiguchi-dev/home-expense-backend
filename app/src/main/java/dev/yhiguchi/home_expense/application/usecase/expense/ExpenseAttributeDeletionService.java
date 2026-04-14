package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseRepository;
import dev.yhiguchi.home_expense.domain.model.expense.Expenses;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseAttributeDeletionService {

  ExpenseAttributeRepository expenseAttributeRepository;
  ExpenseRepository expenseRepository;

  public ExpenseAttributeDeletionService(
      ExpenseAttributeRepository expenseAttributeRepository, ExpenseRepository expenseRepository) {
    this.expenseAttributeRepository = expenseAttributeRepository;
    this.expenseRepository = expenseRepository;
  }

  public void delete(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    ExpenseAttribute attribute = expenseAttributeRepository.get(expenseAttributeIdentifier);
    Expenses expenses = expenseRepository.find(attribute);
    if (expenses.has(attribute)) {
      throw new ExpenseAttributeConstraintException();
    }
    expenseAttributeRepository.delete(expenseAttributeIdentifier);
  }
}
