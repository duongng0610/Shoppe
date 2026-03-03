package com.e_cormerce.shoppe.dto.response;

import com.e_cormerce.shoppe.entity.user.Role;
import com.e_cormerce.shoppe.enums.RoleEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.context.support.BeanDefinitionDsl;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class RegisterResponse {
    String email;
    String hashedPassword;
    String username;
    Role role;
}
