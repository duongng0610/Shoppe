package com.e_cormerce.shoppe.dto.common.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
  String id;
  String username;
  String avatar;
  String role;
}
