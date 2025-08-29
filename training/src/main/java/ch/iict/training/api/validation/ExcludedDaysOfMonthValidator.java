package ch.iict.training.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.util.Arrays;

public class ExcludedDaysOfMonthValidator implements ConstraintValidator<ExcludedDaysOfMonth, LocalDate> {
    private int[] excluded;
    private boolean allowNull;

    @Override
    public void initialize(ExcludedDaysOfMonth ann) {
        this.excluded = ann.value() != null ? ann.value().clone() : new int[0];
        Arrays.sort(this.excluded);
        this.allowNull = ann.allowNull();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext ctx) {
        if (value == null) return allowNull;
        int d = value.getDayOfMonth(); // 1..28/29/30/31
        return Arrays.binarySearch(excluded, d) < 0;
    }
}
