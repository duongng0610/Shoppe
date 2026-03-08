package com.e_cormerce.shoppe.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return s.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-z]).{8,}$");
  }
}
