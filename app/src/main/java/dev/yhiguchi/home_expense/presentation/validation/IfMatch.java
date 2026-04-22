package dev.yhiguchi.home_expense.presentation.validation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import dev.yhiguchi.home_expense.presentation.validation.payload.PreconditionRequired;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@NotNull(payload = PreconditionRequired.class, message = "If-Matchヘッダーは必須です")
@Pattern(regexp = "^\"\\d+\"$", message = "If-Matchヘッダーの形式が不正です")
@Constraint(validatedBy = {})
@Target(PARAMETER)
@Retention(RUNTIME)
@Documented
public @interface IfMatch {
  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
