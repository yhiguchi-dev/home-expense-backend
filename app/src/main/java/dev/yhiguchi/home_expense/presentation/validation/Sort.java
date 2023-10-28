package dev.yhiguchi.home_expense.presentation.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {SortValidator.class})
public @interface Sort {
  String message() default "ソート順が正しくありません";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
