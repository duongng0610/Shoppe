package com.e_cormerce.shoppe.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateVariantRequest {
    @NotBlank(message = "variant id is required")
    String id;

    @NotNull(message = "new quantity  is required")
    @PositiveOrZero
    Integer newQuantity;
}
