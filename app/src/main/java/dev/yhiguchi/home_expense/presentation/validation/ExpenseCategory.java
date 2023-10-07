package dev.yhiguchi.home_expense.presentation.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ExpenseCategoryValidator.class})
public @interface ExpenseCategory {
  String message() default "経費分類が正しくありません";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
