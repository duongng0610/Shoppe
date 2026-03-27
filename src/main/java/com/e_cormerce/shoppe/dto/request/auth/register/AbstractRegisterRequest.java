package com.e_cormerce.shoppe.dto.request.auth.register;

import com.e_cormerce.shoppe.validation.auth.password.StrongPassword;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Valid
public abstract class AbstractRegisterRequest {

  @NotBlank(message = "email of register is required")
  @Email(message = "email of register has wrong type")
  String email;

  @NotBlank(message = "password of register is required")
  @StrongPassword
  String password;

  @NotBlank(message = "username of register is required")
  @Size(min = 6, message = "your username at least 6 character")
  String username;
}
