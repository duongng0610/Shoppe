package com.e_cormerce.shoppe.dto.request.order;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShipCostOrderRequest {

  @NotNull(message = "variant_id  is required")
  String variantId;

  @Valid AddressDto address;
}
