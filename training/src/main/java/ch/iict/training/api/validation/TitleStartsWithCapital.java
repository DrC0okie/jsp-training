package ch.iict.training.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TitleStartsWithCapitalValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface TitleStartsWithCapital {
    String message() default "must start with a capitalized word";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean allowNull() default true;
}
