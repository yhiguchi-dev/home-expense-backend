package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseNotFoundException;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseRepository;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseDataSource implements ExpenseRepository {

  ExpenseMapper expenseMapper;

  public ExpenseDataSource(ExpenseMapper expenseMapper) {
    this.expenseMapper = expenseMapper;
  }

  @Override
  public void register(Expense expense) {
    expenseMapper.insert(expense);
    if (expense.isFixed()) {
      expenseMapper.insertFixedExpense(expense);
    }
    if (expense.isVariable()) {
      expenseMapper.insertVariableExpense(expense);
    }
  }

  @Override
  public Expense get(ExpenseIdentifier expenseIdentifier) {
    Optional<Expense> expense = expenseMapper.selectBy(expenseIdentifier);
    return expense.orElseThrow(ExpenseNotFoundException::new);
  }

  @Override
  public Expense find(ExpenseIdentifier expenseIdentifier) {
    Optional<Expense> expense = expenseMapper.selectBy(expenseIdentifier);
    return expense.orElse(new Expense());
  }

  @Override
  public void update(Expense expense) {
    delete(expense.expenseIdentifier());
    register(expense);
  }

  @Override
  public void delete(ExpenseIdentifier expenseIdentifier) {
    expenseMapper.delete(expenseIdentifier);
  }
}
