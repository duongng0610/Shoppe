package com.e_cormerce.shoppe.event.order.event;

import com.e_cormerce.shoppe.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderTrackingUpdatedEvent {
    String orderId;
    User client;
    String status;
    String address;
}
