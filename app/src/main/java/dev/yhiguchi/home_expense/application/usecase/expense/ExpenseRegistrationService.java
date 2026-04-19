package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeNotFoundException;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseRegistrationService {

  ExpenseRepository expenseRepository;
  ExpenseAttributeRepository expenseAttributeRepository;

  public ExpenseRegistrationService(
      ExpenseRepository expenseRepository, ExpenseAttributeRepository expenseAttributeRepository) {
    this.expenseRepository = expenseRepository;
    this.expenseAttributeRepository = expenseAttributeRepository;
  }

  public ExpenseIdentifier register(ExpenseRegistrationCommand command) {
    ExpenseAttribute attribute =
        expenseAttributeRepository
            .findBy(command.expenseAttributeIdentifier())
            .orElseThrow(ExpenseAttributeNotFoundException::new);
    Expense expense =
        Expense.create(
            command.description(),
            command.price(),
            command.paymentDate(),
            attribute.expenseAttributeIdentifier(),
            attribute.expenseCategory());
    expenseRepository.register(expense);
    return expense.expenseIdentifier();
  }
}
