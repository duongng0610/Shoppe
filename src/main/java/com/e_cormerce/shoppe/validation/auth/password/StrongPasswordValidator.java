package com.e_cormerce.shoppe.validation.auth.password;

import com.e_cormerce.shoppe.util.constants.RegexExpression;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;//để @NotBlank xử lý
        }
        return s.matches(RegexExpression.PASSWORD);
    }
}
