package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseNotFoundException;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseRetrievalService {

  ExpenseRepository expenseRepository;

  public ExpenseRetrievalService(ExpenseRepository expenseRepository) {
    this.expenseRepository = expenseRepository;
  }

  public Revision<Expense> get(ExpenseIdentifier expenseIdentifier) {
    return expenseRepository.findBy(expenseIdentifier).orElseThrow(ExpenseNotFoundException::new);
  }
}
