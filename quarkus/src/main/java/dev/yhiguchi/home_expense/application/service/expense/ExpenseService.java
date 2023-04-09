package dev.yhiguchi.home_expense.application.service.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseRepository;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseService {

  ExpenseRepository expenseRepository;

  public ExpenseService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  public void register(Expense expense) {
    expenseRepository.register(expense);
  }

  public Expense get(ExpenseIdentifier expenseIdentifier) {
    return expenseRepository.get(expenseIdentifier);
  }

  public void update(Expense expense) {
    expenseRepository.update(expense);
  }

  public void delete(ExpenseIdentifier expenseIdentifier) {
    expenseRepository.delete(expenseIdentifier);
  }
}
