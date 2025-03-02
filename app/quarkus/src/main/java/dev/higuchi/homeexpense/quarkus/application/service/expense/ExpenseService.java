package dev.higuchi.homeexpense.quarkus.application.service.expense;

import dev.higuchi.homeexpense.command.model.expense.Expense;
import dev.higuchi.homeexpense.command.model.expense.ExpenseIdentifier;
import dev.higuchi.homeexpense.command.model.expense.ExpenseRepository;
import dev.higuchi.homeexpense.command.model.expense.Expenses;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import jakarta.enterprise.context.ApplicationScoped;

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

  public Expenses find(ExpenseAttribute expenseAttribute) {
    return expenseRepository.find(expenseAttribute);
  }

  public void update(Expense expense) {
    expenseRepository.update(expense);
  }

  public void delete(ExpenseIdentifier expenseIdentifier) {
    expenseRepository.delete(expenseIdentifier);
  }
}
