package com.e_cormerce.shoppe.validation.auth.email;

import com.e_cormerce.shoppe.util.constants.RegexExpression;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;//để @NotBlank xử lý
        }
        return s.matches(RegexExpression.EMAIL);
    }
}
