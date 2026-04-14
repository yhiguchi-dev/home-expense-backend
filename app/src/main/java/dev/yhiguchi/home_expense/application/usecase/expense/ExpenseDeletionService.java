package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseDeletionService {

  ExpenseRepository expenseRepository;

  public ExpenseDeletionService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  public void delete(ExpenseIdentifier expenseIdentifier) {
    expenseRepository.get(expenseIdentifier);
    expenseRepository.delete(expenseIdentifier);
  }
}
