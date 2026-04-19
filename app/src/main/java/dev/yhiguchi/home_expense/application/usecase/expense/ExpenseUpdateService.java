package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeNotFoundException;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseUpdateService {

  ExpenseRepository expenseRepository;
  ExpenseAttributeRepository expenseAttributeRepository;

  public ExpenseUpdateService(
      ExpenseRepository expenseRepository, ExpenseAttributeRepository expenseAttributeRepository) {
    this.expenseRepository = expenseRepository;
    this.expenseAttributeRepository = expenseAttributeRepository;
  }

  public void update(ExpenseUpdateCommand command) {
    Expense expense =
        expenseRepository
            .findBy(command.expenseIdentifier())
            .orElseThrow(ExpenseNotFoundException::new);
    ExpenseAttribute attribute =
        expenseAttributeRepository
            .findBy(command.expenseAttributeIdentifier())
            .orElseThrow(ExpenseAttributeNotFoundException::new);
    Expense updated =
        expense.updateWith(
            command.description(),
            command.price(),
            command.paymentDate(),
            attribute.expenseAttributeIdentifier(),
            attribute.expenseCategory());
    if (expense.hasChanges(updated)) {
      expenseRepository.update(updated.withVersion(command.version()));
    }
  }
}
