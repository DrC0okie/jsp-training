package ch.iict.training.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExcludedDaysOfMonthValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcludedDaysOfMonth {
    String message() default "this day-of-month is not allowed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    /** Liste des jours à exclure (1..31). */
    int[] value();

    boolean allowNull() default true;
}
