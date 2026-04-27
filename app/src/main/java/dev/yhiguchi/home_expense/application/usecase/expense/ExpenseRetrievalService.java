package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseNotFoundException;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseRepository;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeNotFoundException;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeRepository;
import dev.yhiguchi.home_expense.query.expense.ExpenseDetail;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseRetrievalService {

  ExpenseRepository expenseRepository;
  ExpenseAttributeRepository expenseAttributeRepository;

  public ExpenseRetrievalService(
      ExpenseRepository expenseRepository, ExpenseAttributeRepository expenseAttributeRepository) {
    this.expenseRepository = expenseRepository;
    this.expenseAttributeRepository = expenseAttributeRepository;
  }

  public ExpenseDetail get(ExpenseIdentifier expenseIdentifier) {
    Revision<Expense> loaded =
        expenseRepository.findBy(expenseIdentifier).orElseThrow(ExpenseNotFoundException::new);
    Expense expense = loaded.entity();
    ExpenseAttribute attribute =
        expenseAttributeRepository
            .findBy(expense.expenseAttributeIdentifier())
            .map(Revision::entity)
            .orElseThrow(ExpenseAttributeNotFoundException::new);
    return new ExpenseDetail(expense, attribute, loaded.version());
  }
}
