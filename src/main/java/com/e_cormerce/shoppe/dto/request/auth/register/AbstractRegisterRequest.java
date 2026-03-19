package com.e_cormerce.shoppe.dto.request.auth.register;

import com.e_cormerce.shoppe.validation.auth.password.StrongPassword;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @Email
    String email;

    @NotBlank
    @StrongPassword
    String password;


    String username;
}
