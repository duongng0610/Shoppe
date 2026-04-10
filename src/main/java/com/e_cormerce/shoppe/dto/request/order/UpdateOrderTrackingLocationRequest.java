package com.e_cormerce.shoppe.dto.request.order;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOrderTrackingLocationRequest {
  @NotNull(message = "Status is required")
  String orderStatus;

  @NotBlank(message = "Address is required")
  String address;

  @NotNull(message = "Remaining distance is required")
  Long remainingDistance;
}
