package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseAttributeRegistrationService {

  ExpenseAttributeRepository expenseAttributeRepository;
  ExpenseAttributeNameUniqueness expenseAttributeNameUniqueness;

  public ExpenseAttributeRegistrationService(
      ExpenseAttributeRepository expenseAttributeRepository) {
    this.expenseAttributeRepository = expenseAttributeRepository;
    this.expenseAttributeNameUniqueness =
        new ExpenseAttributeNameUniqueness(expenseAttributeRepository::existsByName);
  }

  public ExpenseAttributeIdentifier register(ExpenseAttributeRegistrationCommand command) {
    expenseAttributeNameUniqueness.assertUniqueForRegistration(
        command.expenseAttributeName(), command.expenseCategory());
    ExpenseAttribute expenseAttribute =
        ExpenseAttribute.create(command.expenseAttributeName(), command.expenseCategory());
    expenseAttributeRepository.register(expenseAttribute);
    return expenseAttribute.expenseAttributeIdentifier();
  }
}
