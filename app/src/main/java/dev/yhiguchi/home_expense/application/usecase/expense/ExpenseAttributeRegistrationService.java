package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseAttributeRegistrationService {

  ExpenseAttributeRepository expenseAttributeRepository;

  public ExpenseAttributeRegistrationService(
      ExpenseAttributeRepository expenseAttributeRepository) {
    this.expenseAttributeRepository = expenseAttributeRepository;
  }

  public ExpenseAttributeIdentifier createAndRegister(
      ExpenseAttributeName expenseAttributeName, ExpenseCategory expenseCategory) {
    if (expenseAttributeRepository.existsByName(expenseAttributeName)) {
      throw new ExpenseAttributeAlreadyExistsException();
    }
    ExpenseAttribute expenseAttribute =
        ExpenseAttribute.create(expenseAttributeName, expenseCategory);
    expenseAttributeRepository.register(expenseAttribute);
    return expenseAttribute.expenseAttributeIdentifier();
  }
}
