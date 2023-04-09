package dev.yhiguchi.home_expense.presentation.validation;

import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExpenseCategoryValidator implements ConstraintValidator<ExpenseCategory, String> {
  @Override
  public void initialize(ExpenseCategory constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (Objects.isNull(value)) {
      return true;
    }
    return dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory.hasName(value);
  }
}
