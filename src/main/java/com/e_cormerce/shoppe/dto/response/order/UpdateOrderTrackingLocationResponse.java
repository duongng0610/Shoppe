package com.e_cormerce.shoppe.dto.response.order;

import com.e_cormerce.shoppe.dto.common.order.OrderTrackingLocationDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOrderTrackingLocationResponse {
  OrderTrackingLocationDTO locationDTO;
}
