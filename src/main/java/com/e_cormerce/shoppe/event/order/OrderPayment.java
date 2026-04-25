package com.e_cormerce.shoppe.event.order;

import com.e_cormerce.shoppe.dto.common.user.UserDto;
import com.e_cormerce.shoppe.dto.response.order.OrderDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderPayment {
    OrderDto order;
    UserDto client;
    String sellerId;
    String variantId;
    boolean success;
    LocalDateTime paymentDate;
}
