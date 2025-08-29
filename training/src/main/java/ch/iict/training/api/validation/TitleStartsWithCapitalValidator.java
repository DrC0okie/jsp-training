package ch.iict.training.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TitleStartsWithCapitalValidator implements ConstraintValidator<TitleStartsWithCapital, String> {
    private boolean allowNull;

    @Override
    public void initialize(TitleStartsWithCapital ann) {
        this.allowNull = ann.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if (value == null) return allowNull;
        String s = value.strip(); // trim unicode-aware
        if (s.isEmpty()) return false;

        int i = 0;
        while (i < s.length()) {
            int cp = s.codePointAt(i);
            if (Character.isLetter(cp)) {
                return Character.isUpperCase(cp);
            }
            i += Character.charCount(cp);
        }
        // Pas de lettre → on considère invalide
        return false;
    }
}
