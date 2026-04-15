package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
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

  public ExpenseAttributeIdentifier register(
      ExpenseAttributeName expenseAttributeName, ExpenseCategory expenseCategory) {
    expenseAttributeNameUniqueness.assertUnique(expenseAttributeName);
    ExpenseAttribute expenseAttribute =
        ExpenseAttribute.create(expenseAttributeName, expenseCategory);
    expenseAttributeRepository.register(expenseAttribute);
    return expenseAttribute.expenseAttributeIdentifier();
  }
}
