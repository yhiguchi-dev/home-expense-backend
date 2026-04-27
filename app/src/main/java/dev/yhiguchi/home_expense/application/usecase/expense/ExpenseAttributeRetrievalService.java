package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeNotFoundException;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseAttributeRetrievalService {

  ExpenseAttributeRepository expenseAttributeRepository;

  public ExpenseAttributeRetrievalService(ExpenseAttributeRepository expenseAttributeRepository) {
    this.expenseAttributeRepository = expenseAttributeRepository;
  }

  public Revision<ExpenseAttribute> get(ExpenseAttributeIdentifier expenseIdentifier) {
    return expenseAttributeRepository
        .findBy(expenseIdentifier)
        .orElseThrow(ExpenseAttributeNotFoundException::new);
  }
}
