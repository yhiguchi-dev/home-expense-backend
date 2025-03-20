package dev.higuchi.homeexpense.command.service.expense;

import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;

@FunctionalInterface
public interface ExpenseAttributeFn {
  ExpenseAttribute get(ExpenseAttributeIdentifier identifier);
}
