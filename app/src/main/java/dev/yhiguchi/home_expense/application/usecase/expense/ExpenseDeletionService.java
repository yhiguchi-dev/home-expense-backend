package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseNotFoundException;
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
    Expense expense =
        expenseRepository.findBy(expenseIdentifier).orElseThrow(ExpenseNotFoundException::new);
    expenseRepository.delete(expense);
  }
}
