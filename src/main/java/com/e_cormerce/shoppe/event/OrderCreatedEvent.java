package com.e_cormerce.shoppe.event;

import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderCreatedEvent {
  Order order;
  User client;
  User seller;
  Variant variant;
}
