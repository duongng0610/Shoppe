package com.e_cormerce.shoppe.dto.response.account;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeShipInfoResponse {
  @Valid AddressDto address;

  @NotBlank(message = "phone number is required")
  String phoneNumber;
}
