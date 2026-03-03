package com.e_cormerce.shoppe.dto.request;

import com.e_cormerce.shoppe.enums.RoleEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class RegisterRequest {
    String email;
    String password;
    String username;
    String role;
}
