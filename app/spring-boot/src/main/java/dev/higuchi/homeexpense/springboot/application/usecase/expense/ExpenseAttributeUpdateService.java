package dev.higuchi.homeexpense.springboot.application.usecase.expense;

import dev.higuchi.homeexpense.command.model.expense.ExpenseCategory;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeName;
import dev.higuchi.homeexpense.command.service.expense.ExpenseAttributeUpdater;
import dev.higuchi.homeexpense.springboot.application.service.expense.attribute.ExpenseAttributeService;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class ExpenseAttributeUpdateService {

  ExpenseAttributeService expenseAttributeService;

  public ExpenseAttributeUpdateService(ExpenseAttributeService expenseAttributeService) {
    this.expenseAttributeService = expenseAttributeService;
  }

  public void update(
      ExpenseAttributeIdentifier expenseAttributeIdentifier,
      ExpenseAttributeName expenseAttributeName,
      ExpenseCategory expenseCategory) {
    Function<ExpenseAttributeIdentifier, ExpenseAttribute> getFn =
        identifier -> expenseAttributeService.get(identifier);
    Consumer<ExpenseAttribute> updateFn = attribute -> expenseAttributeService.update(attribute);
    ExpenseAttributeUpdater updater = new ExpenseAttributeUpdater(getFn, updateFn);
    updater.update(expenseAttributeIdentifier, expenseAttributeName, expenseCategory);
  }
}
