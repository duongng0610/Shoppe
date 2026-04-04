package com.e_cormerce.shoppe.dto.response.account;

import java.util.Date;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
  String id;
  String email;
  String username;
  String avatar;
  String role;
  String phoneNumber;
  Date birthDate;
  Date createdAt;
  String province;
  String district;
  String ward;
}
