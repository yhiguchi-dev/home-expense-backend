package dev.yhiguchi.home_expense.presentation.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ExpenseCategoryValidator.class})
public @interface ExpenseCategory {
  String message() default "経費分類が正しくありません";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
