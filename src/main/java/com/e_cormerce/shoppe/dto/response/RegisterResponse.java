package com.e_cormerce.shoppe.dto.response;

import com.e_cormerce.shoppe.entity.user.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
