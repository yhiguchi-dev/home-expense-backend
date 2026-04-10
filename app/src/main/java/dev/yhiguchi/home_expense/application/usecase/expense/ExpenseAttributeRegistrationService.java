package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

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
    Function<ExpenseAttributeName, Optional<ExpenseAttribute>> findFn =
        name -> expenseAttributeRepository.find(name);
    Consumer<ExpenseAttribute> registerFn =
        attribute -> expenseAttributeRepository.register(attribute);
    ExpenseAttributeCreator creator = new ExpenseAttributeCreator(findFn, registerFn);
    ExpenseAttribute expenseAttribute =
        creator.createAndRegister(expenseAttributeName, expenseCategory);
    return expenseAttribute.expenseAttributeIdentifier();
  }
}
