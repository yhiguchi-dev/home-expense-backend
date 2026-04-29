package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
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
    Expense current = expenseRepository.get(command.expenseIdentifier());
    ExpenseAttribute attribute =
        expenseAttributeRepository.get(command.expenseAttributeIdentifier());
    Expense updated =
        current.updateWith(
            command.description(),
            command.amount(),
            command.paymentDate(),
            attribute.expenseAttributeIdentifier());
    if (current.hasChanges(updated)) {
      expenseRepository.update(updated, command.version());
    }
  }
}
