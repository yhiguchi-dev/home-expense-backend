package dev.higuchi.homeexpense.quarkus.application.usecase.expense;

import dev.higuchi.homeexpense.command.model.expense.ExpenseCategory;
import dev.higuchi.homeexpense.command.model.expense.attribute.*;
import dev.higuchi.homeexpense.command.service.expense.ExpenseAttributeCreator;
import dev.higuchi.homeexpense.quarkus.application.service.expense.attribute.ExpenseAttributeService;
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
