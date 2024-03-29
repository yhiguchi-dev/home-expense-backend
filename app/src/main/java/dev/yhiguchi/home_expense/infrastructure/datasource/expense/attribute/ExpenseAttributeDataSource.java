package dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class ExpenseAttributeDataSource implements ExpenseAttributeRepository {

  ExpenseAttributeMapper expenseAttributeMapper;

  public ExpenseAttributeDataSource(ExpenseAttributeMapper expenseAttributeMapper) {
    this.expenseAttributeMapper = expenseAttributeMapper;
  }

  @Override
  public void register(ExpenseAttribute expenseAttribute) {
    expenseAttributeMapper.insert(expenseAttribute);
  }

  @Override
  public ExpenseAttribute get(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    Optional<ExpenseAttribute> expenseAttribute =
        expenseAttributeMapper.selectBy(expenseAttributeIdentifier);
    return expenseAttribute.orElseThrow(ExpenseAttributeNotFoundException::new);
  }

  @Override
  public ExpenseAttribute find(ExpenseAttributeName expenseAttributeName) {
    Optional<ExpenseAttribute> expenseAttribute =
        expenseAttributeMapper.selectByExpenseAttributeName(expenseAttributeName);
    return expenseAttribute.orElse(new ExpenseAttribute());
  }

  @Override
  public void update(ExpenseAttribute expenseAttribute) {
    delete(expenseAttribute.expenseAttributeIdentifier());
    register(expenseAttribute);
  }

  @Override
  public void delete(ExpenseAttributeIdentifier expenseAttributeIdentifier) {
    expenseAttributeMapper.delete(expenseAttributeIdentifier);
  }
}
