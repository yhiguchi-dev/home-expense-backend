package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense;

import dev.higuchi.homeexpense.command.model.expense.*;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import java.util.List;
import java.util.Optional;

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
  public Expenses find(ExpenseAttribute expenseAttribute) {
    Optional<List<Expense>> expense = expenseMapper.selectByExpenseAttribute(expenseAttribute);
    return expense.map(Expenses::new).orElseGet(Expenses::new);
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
