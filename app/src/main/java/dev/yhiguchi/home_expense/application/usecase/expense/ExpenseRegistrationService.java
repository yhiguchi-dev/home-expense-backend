package dev.yhiguchi.home_expense.application.usecase.expense;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ExpenseRegistrationService {

  ExpenseRepository expenseRepository;
  ExpenseAttributeRepository expenseAttributeRepository;

  public ExpenseRegistrationService(
      ExpenseRepository expenseRepository, ExpenseAttributeRepository expenseAttributeRepository) {
    this.expenseRepository = expenseRepository;
    this.expenseAttributeRepository = expenseAttributeRepository;
  }

  public ExpenseIdentifier register(
      Description description,
      Price price,
      PaymentDate paymentDate,
      ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    ExpenseAttribute attribute = expenseAttributeRepository.get(expenseAttributeIdentifier);
    Expense expense = Expense.create(description, price, paymentDate, attribute);
    expenseRepository.register(expense);
    return expense.expenseIdentifier();
  }
}
