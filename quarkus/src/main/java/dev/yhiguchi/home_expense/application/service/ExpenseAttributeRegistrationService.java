package dev.yhiguchi.home_expense.application.service;

import dev.yhiguchi.home_expense.application.service.expense.attribute.ExpenseAttributeService;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class ExpenseAttributeRegistrationService {

  ExpenseAttributeService expenseAttributeService;

  public ExpenseAttributeRegistrationService(ExpenseAttributeService expenseAttributeService) {
    this.expenseAttributeService = expenseAttributeService;
  }

  public ExpenseAttributeIdentifier createAndRegister(
      ExpenseAttributeName expenseAttributeName, ExpenseCategory expenseCategory) {
    Function<ExpenseAttributeName, ExpenseAttribute> findFn =
        name -> expenseAttributeService.find(name);
    Consumer<ExpenseAttribute> registerFn =
        attribute -> expenseAttributeService.register(attribute);
    ExpenseAttributeCreator creator = new ExpenseAttributeCreator(findFn, registerFn);
    ExpenseAttribute expenseAttribute =
        creator.createAndRegister(expenseAttributeName, expenseCategory);
    return expenseAttribute.expenseAttributeIdentifier();
  }
}
