package com.e_cormerce.shoppe.validation.auth.password;

import com.e_cormerce.shoppe.dto.request.account.ChangePasswordRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
    implements ConstraintValidator<PasswordMatches, ChangePasswordRequest> {

  @Override
  public boolean isValid(ChangePasswordRequest request, ConstraintValidatorContext context) {
    return request.getNewPassword().equals(request.getConfirmPassword());
  }
}
