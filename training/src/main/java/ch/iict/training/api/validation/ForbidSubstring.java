package ch.iict.training.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ForbidSubstringValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ForbidSubstring {
    String message() default "must not contain \"{needle}\"";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String substring() default "test";

    boolean ignoreCase() default true;

    boolean allowNull() default true;
}
