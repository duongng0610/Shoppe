package com.e_cormerce.shoppe.dto.response.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    String id;
    String orderName;
    String orderThumbnail;
    BigDecimal totalPrice;
    BigDecimal shipCost;
    BigDecimal priceEach;
    int quantity;
    LocalDateTime createdAt;
}
