package dev.yhiguchi.home_expense.presentation.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Pattern(
    regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
    message = "IDの形式が不正です")
@Constraint(validatedBy = {})
@Target({FIELD, PARAMETER, RECORD_COMPONENT})
@Retention(RUNTIME)
@Documented
public @interface UuidFormat {
  @OverridesAttribute(constraint = Pattern.class, name = "message")
  String message() default "IDの形式が不正です";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
