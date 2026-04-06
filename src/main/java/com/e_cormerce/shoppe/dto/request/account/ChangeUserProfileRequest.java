package com.e_cormerce.shoppe.dto.request.account;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeUserProfileRequest {
  LocalDate dob;
  String username;
  @Valid AddressDto address;
  String phoneNumber;
}
