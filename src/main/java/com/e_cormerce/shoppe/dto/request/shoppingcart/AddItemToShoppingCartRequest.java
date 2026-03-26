package com.e_cormerce.shoppe.dto.request.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddItemToShoppingCartRequest {
  @NotBlank(message = "variant_id is required")
  String variantId;

  @Min(value = 1, message = "quantity must be >= 1")
  int quantity;
}
