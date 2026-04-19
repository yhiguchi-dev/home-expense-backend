package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeNameUniqueness;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeNotFoundException;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseAttributeUpdateService {

  ExpenseAttributeRepository expenseAttributeRepository;
  ExpenseAttributeNameUniqueness expenseAttributeNameUniqueness;

  public ExpenseAttributeUpdateService(ExpenseAttributeRepository expenseAttributeRepository) {
    this.expenseAttributeRepository = expenseAttributeRepository;
    this.expenseAttributeNameUniqueness =
        new ExpenseAttributeNameUniqueness(expenseAttributeRepository::existsByName);
  }

  public void update(ExpenseAttributeUpdateCommand command) {
    ExpenseAttribute attribute =
        expenseAttributeRepository
            .findBy(command.expenseAttributeIdentifier())
            .orElseThrow(ExpenseAttributeNotFoundException::new);
    if (!attribute.hasSameName(command.expenseAttributeName())) {
      expenseAttributeNameUniqueness.assertUnique(command.expenseAttributeName());
    }
    ExpenseAttribute updated =
        attribute.updateWith(command.expenseAttributeName(), command.expenseCategory());
    if (attribute.hasChanges(updated)) {
      expenseAttributeRepository.update(updated.withVersion(command.version()));
    }
  }
}
