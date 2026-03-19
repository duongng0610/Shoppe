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
    @NotBlank(message = "Email must not be blank")//cần đătj lên trên
    @Email
    String email;

    @NotBlank(message = "Password must not be blank")
    @StrongPassword
    String password;
}
