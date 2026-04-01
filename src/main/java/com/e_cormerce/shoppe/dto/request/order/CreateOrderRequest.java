package com.e_cormerce.shoppe.dto.request.order;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderRequest {
  @NotBlank(message = "variant id is required")
  String variantId;

  @NotNull(message = "shipping address is required")
  @Valid
  AddressDto shippingAddress;

  @NotBlank(message = "phone number is required")
  String shippingPhoneNumber;

  @NotNull(message = "order quantity is required")
  @Min(value = 1, message = "quantity must be >= 1")
  int quantity;
}
