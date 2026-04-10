package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class ExpenseAttributeUpdateService {

  ExpenseAttributeRepository expenseAttributeRepository;

  public ExpenseAttributeUpdateService(ExpenseAttributeRepository expenseAttributeRepository) {
    this.expenseAttributeRepository = expenseAttributeRepository;
  }

  public void update(
      ExpenseAttributeIdentifier expenseAttributeIdentifier,
      ExpenseAttributeName expenseAttributeName,
      ExpenseCategory expenseCategory) {
    Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn =
        identifier -> expenseAttributeRepository.get(identifier);
    Consumer<ExpenseAttribute> updateFn = attribute -> expenseAttributeRepository.update(attribute);
    ExpenseAttributeUpdater updater = new ExpenseAttributeUpdater(getFn, updateFn);
    updater.update(expenseAttributeIdentifier, expenseAttributeName, expenseCategory);
  }
}
