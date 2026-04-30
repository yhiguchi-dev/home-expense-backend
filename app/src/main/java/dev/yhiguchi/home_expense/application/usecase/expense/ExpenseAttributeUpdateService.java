package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeNameUniqueness;
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
        new ExpenseAttributeNameUniqueness(expenseAttributeRepository);
  }

  public void update(ExpenseAttributeUpdateCommand command) {
    ExpenseAttribute current = expenseAttributeRepository.get(command.expenseAttributeIdentifier());
    expenseAttributeNameUniqueness.assertUniqueForUpdate(current, command.expenseAttributeName());
    ExpenseAttribute updated = current.updateWith(command.expenseAttributeName());
    if (current.hasChanges(updated)) {
      expenseAttributeRepository.update(updated, command.version());
    }
  }
}
