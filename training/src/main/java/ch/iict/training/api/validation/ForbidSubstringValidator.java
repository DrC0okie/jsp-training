package ch.iict.training.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ForbidSubstringValidator implements ConstraintValidator<ForbidSubstring, String> {
    private String needle;
    private boolean ignoreCase;
    private boolean allowNull;

    @Override
    public void initialize(ForbidSubstring ann) {
        this.needle = ann.substring();
        this.ignoreCase = ann.ignoreCase();
        this.allowNull = ann.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if (value == null) return allowNull;
        if (needle == null || needle.isEmpty()) return true;
        if (ignoreCase) {
            return !value.toLowerCase().contains(needle.toLowerCase());
        }
        return !value.contains(needle);
    }
}
