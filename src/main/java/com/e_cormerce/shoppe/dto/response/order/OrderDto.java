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
    String productName;
    String thumbnail;
    BigDecimal totalPrice;
    int quantity;
    LocalDateTime createdAt;
}
