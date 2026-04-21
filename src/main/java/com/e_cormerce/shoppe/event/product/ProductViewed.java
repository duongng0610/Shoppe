package com.e_cormerce.shoppe.event.product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductViewed {
    String productId;
    LocalDateTime viewedAt;
}
