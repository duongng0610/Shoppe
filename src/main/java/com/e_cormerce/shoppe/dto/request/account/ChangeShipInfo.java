package com.e_cormerce.shoppe.dto.request.account;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeShipInfo {
  @Valid AddressDto address;
  String phoneNumber;
}
