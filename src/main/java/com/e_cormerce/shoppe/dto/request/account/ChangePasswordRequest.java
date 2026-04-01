package com.e_cormerce.shoppe.dto.request.account;

import com.e_cormerce.shoppe.validation.auth.password.PasswordMatches;
import com.e_cormerce.shoppe.validation.auth.password.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PasswordMatches
public class ChangePasswordRequest {
  @NotBlank(message = "new password is required")
  @StrongPassword
  String newPassword;

  @NotBlank(message = "confirm password is required")
  String confirmPassword;

  @NotBlank(message = "old password is required")
  String oldPassword;
}
