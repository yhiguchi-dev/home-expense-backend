package dev.yhiguchi.home_expense.presentation.validation;

import dev.yhiguchi.home_expense.query.expense.SortOrder;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class SortValidator implements ConstraintValidator<Sort, String> {
  @Override
  public void initialize(Sort constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (Objects.isNull(value)) {
      return true;
    }
    return SortOrder.has(value);
  }
}
