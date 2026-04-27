package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.Revision;
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
    Revision<Expense> loaded =
        expenseRepository
            .findBy(command.expenseIdentifier())
            .orElseThrow(ExpenseNotFoundException::new);
    ExpenseAttribute attribute =
        expenseAttributeRepository
            .findBy(command.expenseAttributeIdentifier())
            .map(Revision::entity)
            .orElseThrow(ExpenseAttributeNotFoundException::new);
    Expense current = loaded.entity();
    Expense updated =
        current.updateWith(
            command.description(),
            command.price(),
            command.paymentDate(),
            attribute.expenseAttributeIdentifier(),
            attribute.expenseCategory());
    if (current.hasChanges(updated)) {
      expenseRepository.update(new Revision<>(updated, command.version()));
    }
  }
}
