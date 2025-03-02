package dev.higuchi.homeexpense.quarkus.application.service.expense.attribute;

import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeName;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseAttributeService {

  ExpenseAttributeRepository expenseAttributeRepository;

  public ExpenseAttributeService(ExpenseAttributeRepository expenseAttributeRepository) {
    this.expenseAttributeRepository = expenseAttributeRepository;
  }

  public void register(ExpenseAttribute expenseAttribute) {
    expenseAttributeRepository.register(expenseAttribute);
  }

  public ExpenseAttribute get(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    return expenseAttributeRepository.get(expenseAttributeIdentifier);
  }

  public ExpenseAttribute find(ExpenseAttributeName expenseAttributeName) {
    return expenseAttributeRepository.find(expenseAttributeName);
  }

  public void update(ExpenseAttribute expenseAttribute) {
    expenseAttributeRepository.update(expenseAttribute);
  }

  public void delete(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    expenseAttributeRepository.delete(expenseAttributeIdentifier);
  }
}
