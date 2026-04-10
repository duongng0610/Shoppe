package com.e_cormerce.shoppe.dto.common.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderTrackingLocationDTO {
  @NotBlank(message = "address is required")
  String address;

  @NotNull(message = "arrived_at is required")
  LocalDateTime arrivedAt;

  @NotNull(message = "remaining_distance is required")
  Long remainingDistance;
}
