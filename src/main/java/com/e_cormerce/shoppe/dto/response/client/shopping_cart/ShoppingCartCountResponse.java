package com.e_cormerce.shoppe.dto.response.client.shopping_cart;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCartCountResponse {
    String id;
    long count;
}
