package com.e_cormerce.shoppe.dto.response.account;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeUserProfileResponse {
  @NotBlank(message = "username is required")
  String username;

  LocalDate dob;
  String avatar;
}
