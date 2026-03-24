package com.e_cormerce.shoppe.dto.request.auth.login;

import com.e_cormerce.shoppe.validation.auth.password.StrongPassword;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Valid
public class LogInRequest {
  @NotBlank(message = "LoginRequest must not be blank")
  @Email(message = "Invalid Email")
  String email;

  @NotBlank(message = "LoginRequest must not be blank")
  @StrongPassword
  String password;
}
