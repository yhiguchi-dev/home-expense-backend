package dev.higuchi.homeexpense.springboot.application.usecase.expense;

import dev.higuchi.homeexpense.command.model.expense.ExpenseCategory;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeName;
import dev.higuchi.homeexpense.command.service.expense.ExpenseAttributeCreator;
import dev.higuchi.homeexpense.springboot.application.service.expense.attribute.ExpenseAttributeService;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
