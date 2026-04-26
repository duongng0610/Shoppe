package com.e_cormerce.shoppe.dto.response.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderResponse {
  String orderId;
  String sellerId;
  String clientId;
  String paymentUrl;
}
