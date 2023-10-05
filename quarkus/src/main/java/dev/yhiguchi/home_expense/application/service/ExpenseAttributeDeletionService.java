package dev.yhiguchi.home_expense.application.service;

import dev.yhiguchi.home_expense.application.service.expense.attribute.ExpenseAttributeService;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class ExpenseAttributeDeletionService {

  ExpenseAttributeService expenseAttributeService;

  public ExpenseAttributeDeletionService(ExpenseAttributeService expenseAttributeService) {
    this.expenseAttributeService = expenseAttributeService;
  }

  public void delete(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn =
        identifier -> expenseAttributeService.get(identifier);
    Consumer<ExpenseAttributeIdentifier> deleteFn =
        attribute -> expenseAttributeService.delete(expenseAttributeIdentifier);
    ExpenseAttributeDeleter deleter = new ExpenseAttributeDeleter(getFn, deleteFn);
    deleter.delete(expenseAttributeIdentifier);
  }
}
