package com.e_cormerce.shoppe.dto.request;

import com.e_cormerce.shoppe.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class RegisterRequest {
    @NotNull
    String email;
    @NotNull
    String password;
    @NotNull
    String username;
    @NotNull
    String role;
}
